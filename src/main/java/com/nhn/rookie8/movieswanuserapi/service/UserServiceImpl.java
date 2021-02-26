package com.nhn.rookie8.movieswanuserapi.service;

import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.entity.User;
import com.nhn.rookie8.movieswanuserapi.repository.UserRepository;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;


    @Override
    public void register(UserDTO dto){

        dto.setRegDate(LocalDateTime.now());
        dto.setModDate(LocalDateTime.now());
        User entity = dtoToEntity(dto);
        userRepository.save(entity);

    }

    @Override
    public void update(UserDTO dto){

        dto.setModDate(LocalDateTime.now());
        User entity = dtoToEntity(dto);
        userRepository.save(entity);

    }

    @Override
    public ResponseDTO returnResponseDto(ErrorCode errorCode, UserDTO userDTO){

        return ResponseDTO.builder()
                .httpCode(errorCode==ErrorCode.NO_ERROR?200:400)
                .error(errorCode!=ErrorCode.NO_ERROR)
                .errorCode(errorCode.ordinal())
                .message(errorCode.getMessage())
                .content(userDTO)
                .build();
    }

    @Override
    public UserDTO getUserInfoById(String uid){

        Optional<User> result = userRepository.findById(uid);

        User user;
        UserDTO userDTO = null;

        if(result.isPresent()){
            user = result.get();
            userDTO = entityToDto(user);
        }

        return userDTO;
    }

    @Override
    public void deleteById(String uid){

        Optional<User> result = userRepository.findById(uid);

        if(result.isPresent()){
            userRepository.deleteById(uid);
        }

    }
}
