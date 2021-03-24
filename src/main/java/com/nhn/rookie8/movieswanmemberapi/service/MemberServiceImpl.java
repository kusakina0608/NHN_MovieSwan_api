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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    private final SecretAccountDataDTO databaseInfoDTO;

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
        String secretKey = databaseInfoDTO.getSecretKey();
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
    public void register(MemberRegisterDTO dto){
        databaseSelector.indicateDB(dto.getMemberId());
        memberRepository.save(
                dtoToEntity(
                        MemberDTO.builder()
                                .memberId(dto.getMemberId())
                                .password(passwordEncoder.encode(dto.getPassword()))
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
    public boolean alreadyMemberExist(String memberId){
        databaseSelector.indicateDB(memberId);
        return memberRepository.findById(memberId).isPresent();
    }


    @Override
    public MemberIdNameDTO authenticate(MemberAuthDTO request){
        databaseSelector.indicateDB(request.getMemberId());
        return memberRepository.findById(request.getMemberId())
                .filter(member -> member.getPassword().equals(request.getPassword()))
                .map(this::entityToMemberIdNameDto).orElseThrow(IdOrPasswordErrorException::new);
    }

    @Override
    public MemberIdNameDTO externalAuthenticate(MemberAuthDTO request){
        databaseSelector.indicateDB(request.getMemberId());
        return memberRepository.findById(request.getMemberId())
                .filter(member -> passwordEncoder.matches(request.getPassword(), member.getPassword()))
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
    public MemberDTO getMemberInfoById(String memberId){
        databaseSelector.indicateDB(memberId);
        return memberRepository.findById(memberId).map(this::entityToDto).orElseThrow(IdOrPasswordErrorException::new);
    }


    @Override
    public MemberIdNameDTO getMemberIdNameDTO(String memberId) {
        databaseSelector.indicateDB(memberId);
        return memberRepository.findById(memberId).isPresent() ?
                entityToMemberIdNameDto(memberRepository.findById(memberId).get()) :
                null;
    }

}
