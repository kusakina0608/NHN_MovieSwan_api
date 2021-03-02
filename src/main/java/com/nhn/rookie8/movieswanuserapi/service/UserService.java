package com.nhn.rookie8.movieswanuserapi.service;

import com.nhn.rookie8.movieswanuserapi.dto.*;
import com.nhn.rookie8.movieswanuserapi.entity.User;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    boolean check(Object request);

    boolean checkString(String request);

    void register(UserRegisterDTO dto);

    boolean alreadyUserExist(UserRegisterDTO request);

    UserIdNameDTO authenticate(UserAuthDTO request);

    UserDTO getUserInfoById(String uid);

    ResponseDTO responseWithContent(ErrorCode errorCode, Object content);

    ResponseDTO responseWithoutContent(ErrorCode errorCode);

    default User dtoToEntity(UserDTO dto){
        return User.builder()
                .uid(dto.getUid())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .url(dto.getUrl())
                .regDate(dto.getRegDate())
                .modDate(dto.getModDate())
                .build();
    }

    default UserDTO entityToDto(User entity){

        return UserDTO.builder()
                .uid(entity.getUid())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .url(entity.getUrl())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    default UserIdNameDTO entityToUserIdNameDto(User entity){

        return UserIdNameDTO.builder()
                .uid(entity.getUid())
                .name(entity.getName())
                .build();
    }
}
