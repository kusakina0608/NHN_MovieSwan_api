package com.nhn.rookie8.movieswanuserapi.service;

import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.entity.User;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Long register(UserDTO dto);

    Long update(UserDTO dto);

    UserDTO getUserInfoById(String uid);

    ResponseDTO returnResponseDto(ErrorCode errorCode, UserDTO userDTO);

    void deleteById(String uid);

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
}
