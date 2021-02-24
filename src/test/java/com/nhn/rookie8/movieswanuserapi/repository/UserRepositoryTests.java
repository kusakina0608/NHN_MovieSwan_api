package com.nhn.rookie8.movieswanuserapi.repository;

import com.nhn.rookie8.movieswanuserapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    void testInsertDummies(){

        IntStream.rangeClosed(1,1000).forEach(i->{
            User user = User
                    .builder()
                    .uid("" + i)
                    .password("" + i)
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
