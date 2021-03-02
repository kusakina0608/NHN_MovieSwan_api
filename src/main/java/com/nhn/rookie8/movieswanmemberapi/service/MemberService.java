package com.nhn.rookie8.movieswanmemberapi.service;

import com.nhn.rookie8.movieswanmemberapi.dto.*;
import com.nhn.rookie8.movieswanmemberapi.entity.Member;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    boolean check(Object request);

    boolean checkString(String request);

    void register(MemberRegisterDTO dto);

    boolean alreadyUserExist(MemberRegisterDTO request);

    MemberIdNameDTO authenticate(MemberAuthDTO request);

    MemberDTO getUserInfoById(String uid);

    ResponseDTO responseWithContent(ErrorCode errorCode, Object content);

    ResponseDTO responseWithoutContent(ErrorCode errorCode);

    default Member dtoToEntity(MemberDTO dto){
        return Member.builder()
                .memberId(dto.getMemberId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .url(dto.getUrl())
                .regDate(dto.getRegDate())
                .modDate(dto.getModDate())
                .build();
    }

    default MemberDTO entityToDto(Member entity){

        return MemberDTO.builder()
                .memberId(entity.getMemberId())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .url(entity.getUrl())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    default MemberIdNameDTO entityToUserIdNameDto(Member entity){

        return MemberIdNameDTO.builder()
                .memberId(entity.getMemberId())
                .name(entity.getName())
                .build();
    }
}
