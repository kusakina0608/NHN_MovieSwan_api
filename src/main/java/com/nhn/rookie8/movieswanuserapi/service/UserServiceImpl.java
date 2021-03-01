package com.nhn.rookie8.movieswanuserapi.service;

import com.nhn.rookie8.movieswanuserapi.dto.*;
import com.nhn.rookie8.movieswanuserapi.repository.UserRepository;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;


    @Override
    public boolean check(UserBasicDTO request) {
        return request.getUid() != null && !request.getUid().trim().isEmpty()
                && request.getPassword() != null && !request.getPassword().trim().isEmpty()
                && request.getName() != null && !request.getName().trim().isEmpty()
                && request.getEmail() != null && !request.getEmail().trim().isEmpty()
                && request.getUrl() != null && !request.getUrl().trim().isEmpty();
    }

    @Override
    public boolean check(UserAuthDTO request) {
        return request.getUid() != null && !request.getUid().trim().isEmpty()
                && request.getPassword() != null && !request.getPassword().trim().isEmpty();
    }

    @Override
    public boolean check(UserIdDTO request) {
        return request.getUid() != null && !request.getUid().trim().isEmpty();
    }

    @Override
    public boolean check(String request) {
        return request != null && !request.trim().isEmpty();
    }

    @Override
    public void register(UserBasicDTO dto){

        userRepository.save(
                dtoToEntity(
                        UserDTO.builder()
                                .uid(dto.getUid())
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
    public boolean alreadyUserExist(UserBasicDTO request){
        return userRepository.findById(request.getUid()).isPresent();
    }

    @Override
    public boolean authenticate(UserAuthDTO request){
        return userRepository.findById(request.getUid())
                .filter(user -> user.getPassword().equals(request.getPassword())).isPresent();
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
    public UserDTO getUserInfoById(String uid){
        return userRepository.findById(uid).map(this::entityToDto).orElse(null);
    }

}
