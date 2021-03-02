package com.nhn.rookie8.movieswanuserapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanuserapi.dto.*;
import com.nhn.rookie8.movieswanuserapi.repository.UserRepository;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Override
    public boolean check(Object request){
        return objectMapper.convertValue(request, new TypeReference<Map<String,String>>() {})
                .values().stream().allMatch(this::checkString);
    }


    @Override
    public boolean checkString(String request) {
        return request != null && !request.trim().isEmpty();
    }

    @Override
    public void register(UserRegisterDTO dto){

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
    public boolean alreadyUserExist(UserRegisterDTO request){
        return userRepository.findById(request.getUid()).isPresent();
    }


    @Override
    public UserIdNameDTO authenticate(UserAuthDTO request){
        return userRepository.findById(request.getUid())
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .map(this::entityToUserIdNameDto).orElse(null);
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
