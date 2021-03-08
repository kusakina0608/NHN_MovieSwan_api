package com.nhn.rookie8.movieswanmemberapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanmemberapi.dto.*;
import com.nhn.rookie8.movieswanmemberapi.repository.MemberRepository;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

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
                .map(this::entityToMemberIdNameDto).orElse(null);
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
