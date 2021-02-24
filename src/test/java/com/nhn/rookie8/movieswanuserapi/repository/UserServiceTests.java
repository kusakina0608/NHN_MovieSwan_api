package com.nhn.rookie8.movieswanuserapi.repository;

import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTests {

    @Autowired
    private UserService service;

    @Test
    void testRegister(){

        UserDTO userDTO = UserDTO.builder()
                .uid("id")
                .password("pw")
                .name("na")
                .email("a@b.c")
                .url("http")
                .build();

        System.out.println(service.register(userDTO));
    }
}
