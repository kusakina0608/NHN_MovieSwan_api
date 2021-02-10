package com.nhn.rookie8.movieswanuserapi.repository;

import com.nhn.rookie8.movieswanuserapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testClass(){
        System.out.println(userRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){

        IntStream.rangeClosed(1,100).forEach(i->{
            User user = User
                    .builder()
                    .uid("uid.." + i)
                    .password("password.." + i)
                    .name("name.." + i)
                    .email("email.." + i)
                    .url("url.." + i)
                    .regDate(LocalDateTime.now())
                    .modDate(LocalDateTime.now())
                    .build();

            userRepository.save(user);

        });
    }
}
