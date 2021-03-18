package com.nhn.rookie8.movieswanmemberapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanmemberapi.datasource.DatabaseSelector;
import com.nhn.rookie8.movieswanmemberapi.dto.*;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import com.nhn.rookie8.movieswanmemberapi.memberexception.IdOrPasswordErrorException;
import com.nhn.rookie8.movieswanmemberapi.memberexception.UnauthorizedException;
import com.nhn.rookie8.movieswanmemberapi.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final ObjectMapper objectMapper;

    private final DatabaseSelector databaseSelector;

    @Value("${redirectUrl}")
    String redirectUrl;

    @Override
    public boolean checkInput(Object request){
        return request != null && objectMapper.convertValue(request, new TypeReference<Map<String,String>>() {})
                .values().stream().allMatch(this::checkString);
    }


    @Override
    public boolean checkString(String request) { return request != null && !request.trim().isEmpty(); }

    @Override
    public String getToken(String memberId) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("MEMBER");

        String token = Jwts
                .builder()
                .setId("MovieSwanJWT")
                .setSubject(memberId)
                // 권한을 내장하는 게 가장 좋은 practice 입니다. claim을 의미 있게 구성했다는 점에서 칭찬합니다.
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // TODO: Expiration time 을 짧게 줄일 필요가 있습니다, 상수화 하세요 -> 30 * 60 * 1000, date를 쓰지 않아도 됩니다.
                // Date 가 mutable 객체이기 때문에...
                .setExpiration(new Date(System.currentTimeMillis() + 1800000))  // Expiration time: 30 min
                // 해시 알고리즘을 HS512로 선택한 이유??
                .signWith(SignatureAlgorithm.HS512,
                        // TODO: secretKey에서 getBytes를 이용하면 너무 쉽다. 바이트를 난수화시켜서 (Byte Encoding) base64, hex 방식 등
                        secretKey.getBytes()).compact();

        return token;
    }

    @Override
    // TODO: 다 지우세요
    @Synchronized
    public void register(MemberRegisterDTO dto){
        databaseSelector.setDbIndicator(dto.getMemberId());
        memberRepository.save(
                dtoToEntity(
                        MemberDTO.builder()
                                .memberId(dto.getMemberId())
                                .password(dto.getPassword())
                                .name(dto.getName())
                                .email(dto.getEmail())
                                .url(dto.getUrl())
                                .regDate(LocalDateTime.now())
                                .modDate(LocalDateTime.now())
                                .build()
                )
        );

    }


    @Override
    @Synchronized
    public boolean alreadyMemberExist(String memberId){
        databaseSelector.setDbIndicator(memberId);
        return memberRepository.findById(memberId).isPresent();
    }


    @Override
    @Synchronized
    public MemberIdNameDTO authenticate(MemberAuthDTO request){
        databaseSelector.setDbIndicator(request.getMemberId());
        return memberRepository.findById(request.getMemberId())
                .filter(member -> member.getPassword().equals(request.getPassword()))
                .map(this::entityToMemberIdNameDto).orElseThrow(IdOrPasswordErrorException::new);
    }

    @Override
    // TODO: Syncronized 는 필요가 없습니다. 동기화 영역을 길게 잡는 것에 대해 생각을 여러 번 하는 것이 좋습니다.
    // TODO: ThreadLocal 에 대해 루키들에게 공유해 주면 좋을 것 같습니다.
    @Synchronized
    public MemberIdNameDTO externalAuthenticate(MemberAuthDTO request){
        // TODO: indicateDB 가 더 좋은 표현입니다, DB indicator 는 비즈니스 로직이 아닙니다. 도메인 로직은 종단 관심사, 성능, 보안과 관련된 부분을 횡단 관심사라고 합니다.
        // 하마, 여우에서 이 문제를 AOP 라는 기술을 통해 잘 해결했습니다. 필터를 통해 이 문제를 해결해 보았으면 좋겠습니다.
        databaseSelector.setDbIndicator(request.getMemberId());
        return memberRepository.findById(request.getMemberId())
                .filter(member -> member.getPassword().equals(request.getPassword()))
                .map(this::entityToMemberIdNameDto).orElseThrow(UnauthorizedException::new);
    }


    @Override
    public ResponseDTO responseWithContent(ErrorCode errorCode, Object content){

        return ResponseDTO.builder()
                .httpCode(errorCode==ErrorCode.NO_ERROR?200:400)
                .error(errorCode!=ErrorCode.NO_ERROR)
                .errorCode(errorCode.ordinal())
                .message(errorCode.getMessage())
                .content(content)
                .build();
    }


    @Override
    public ResponseDTO responseWithoutContent(ErrorCode errorCode){

        return ResponseDTO.builder()
                .httpCode(errorCode==ErrorCode.NO_ERROR?200:400)
                .error(errorCode!=ErrorCode.NO_ERROR)
                .errorCode(errorCode.ordinal())
                .message(errorCode.getMessage())
                .build();
    }


    @Override
    public TokenDTO responseWithToken(String url){
        return TokenDTO.builder().url(redirectUrl + "?token=" + url).build();
    }


    @Override
    @Synchronized
    public MemberDTO getMemberInfoById(String memberId){
        databaseSelector.setDbIndicator(memberId);
        return memberRepository.findById(memberId).map(this::entityToDto).orElseThrow(IdOrPasswordErrorException::new);
    }


    @Override
    @Synchronized
    public MemberIdNameDTO getMemberIdNameDTO(String memberId) {
        databaseSelector.setDbIndicator(memberId);
        return memberRepository.findById(memberId).isPresent() ?
                entityToMemberIdNameDto(memberRepository.findById(memberId).get()) :
                null;
    }

}
