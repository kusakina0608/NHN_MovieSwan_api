package com.nhn.rookie8.movieswanmemberapi.repository;

import com.nhn.rookie8.movieswanmemberapi.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void testInsertDummies(){

        IntStream.rangeClosed(1,1000).forEach(i->{
            Member member = Member
                    .builder()
                    .memberId("" + i)
                    .password("" + i)
                    .name("name.." + i)
                    .email("email.." + i)
                    .url("url.." + i)
                    .regDate(LocalDateTime.now())
                    .modDate(LocalDateTime.now())
                    .build();

            memberRepository.save(member);

        });
    }
}
