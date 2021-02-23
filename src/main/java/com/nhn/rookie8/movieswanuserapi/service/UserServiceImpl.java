package com.nhn.rookie8.movieswanuserapi.service;

import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.entity.User;
import com.nhn.rookie8.movieswanuserapi.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public Long register(UserDTO dto){

        //log.info("DTO-----------------");
        //log.info(dto);

        User entity = dtoToEntity(dto);

        //log.info(entity);

        //System.out.println(entity);

        userRepository.save(entity);

        return null;
    }

    @Override
    public Long update(UserDTO dto){
        return register(dto);
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
