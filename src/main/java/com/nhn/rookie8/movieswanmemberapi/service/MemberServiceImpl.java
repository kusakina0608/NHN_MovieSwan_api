package com.nhn.rookie8.movieswanmemberapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanmemberapi.dto.*;
import com.nhn.rookie8.movieswanmemberapi.entity.Member;
import com.nhn.rookie8.movieswanmemberapi.memberexception.IdOrPasswordErrorException;
import com.nhn.rookie8.movieswanmemberapi.repository.MemberRepository;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1800000))  // Expiration time: 30 min
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

    @Override
    public void register(MemberRegisterDTO dto){

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
    public boolean alreadyMemberExist(MemberRegisterDTO request){
        return memberRepository.findById(request.getMemberId()).isPresent();
    }


    @Override
    public MemberIdNameDTO authenticate(MemberAuthDTO request){
        return memberRepository.findById(request.getMemberId())
                .filter(member -> member.getPassword().equals(request.getPassword()))
                .map(this::entityToMemberIdNameDto).orElseThrow(IdOrPasswordErrorException::new);
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
    public MemberDTO getMemberInfoById(String memberId){
        return memberRepository.findById(memberId).map(this::entityToDto).orElse(null);
    }

}
