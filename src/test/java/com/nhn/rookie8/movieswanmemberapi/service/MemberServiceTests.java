package com.nhn.rookie8.movieswanmemberapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanmemberapi.dto.*;
import com.nhn.rookie8.movieswanmemberapi.entity.Member;
import com.nhn.rookie8.movieswanmemberapi.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest({MemberService.class, MemberServiceImpl.class})
public class MemberServiceTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    EasyRandom generator;

    @MockBean
    private MemberRepository memberRepository;



    private List<String> dummyMemberIdList;

    private List<MemberDTO> dummyMemberDTOList;

    private List<MemberRegisterDTO> dummyMemberRegisterDTOList;

    private List<MemberAuthDTO> dummyMemberAuthDTOList;

    private List<MemberIdNameDTO> dummyMemberIdNameDTOList;

    private List<MemberIdDTO> dummyMemberIdDTOList;

    private List<ResponseDTO> dummyResponseDTOList;




    @BeforeAll
    public void setUp(){
        generator = new EasyRandom();
        int dummySize = 5;
        dummyMemberIdList = generator.objects(String.class,dummySize).collect(Collectors.toList());
        dummyMemberDTOList = generator.objects(MemberDTO.class,dummySize).collect(Collectors.toList());
        dummyMemberRegisterDTOList = generator.objects(MemberRegisterDTO.class,dummySize).collect(Collectors.toList());
        dummyMemberAuthDTOList = generator.objects(MemberAuthDTO.class,dummySize).collect(Collectors.toList());
        dummyMemberIdNameDTOList = generator.objects(MemberIdNameDTO.class,dummySize).collect(Collectors.toList());
        dummyMemberIdDTOList = generator.objects(MemberIdDTO.class,dummySize).collect(Collectors.toList());
        dummyResponseDTOList = generator.objects(ResponseDTO.class,dummySize).collect(Collectors.toList());
        dummyResponseDTOList.forEach(item->item.setContent(null));
    }

    @AfterAll
    public void tearDown(){
        log.info("teardown");
    }

    @Test
    void checkStringTest(){
        boolean testToBeTrue1 = memberService.checkString(dummyMemberIdList.get(0));

        boolean testToBeFalse1 = memberService.checkString("");
        boolean testToBeFalse2 = memberService.checkString(null);

        Assertions.assertEquals(true, testToBeTrue1, "ok");
        Assertions.assertEquals(false, testToBeFalse1, "ok");
        Assertions.assertEquals(false, testToBeFalse2, "ok");
    }


    @Test
    void checkInputTest(){
        boolean testToBeTrue1 = memberService.checkInput(dummyMemberAuthDTOList.get(0));
        boolean testToBeTrue2 = memberService.checkInput(dummyMemberDTOList.get(0));
        boolean testToBeTrue3 = memberService.checkInput(dummyMemberIdDTOList.get(0));
        boolean testToBeTrue4 = memberService.checkInput(dummyMemberIdNameDTOList.get(0));
        boolean testToBeTrue5 = memberService.checkInput(dummyMemberRegisterDTOList.get(0));

        boolean testToBeFalse1 = memberService.checkInput(MemberRegisterDTO.builder().build());
        boolean testToBeFalse2 = memberService.checkInput(MemberDTO.builder().build());
        boolean testToBeFalse3 = memberService.checkInput(null);

        Assertions.assertEquals(true, testToBeTrue1, "ok");
        Assertions.assertEquals(true, testToBeTrue2, "ok");
        Assertions.assertEquals(true, testToBeTrue3, "ok");
        Assertions.assertEquals(true, testToBeTrue4, "ok");
        Assertions.assertEquals(true, testToBeTrue5, "ok");

        Assertions.assertEquals(false, testToBeFalse1, "ok");
        Assertions.assertEquals(false, testToBeFalse2, "ok");
        Assertions.assertEquals(false, testToBeFalse3, "ok");
    }

    @Test
    void registerTest(){

        dummyMemberRegisterDTOList.forEach(i->memberService.register(i));
        verify(memberRepository,times(5)).save(any(Member.class));

    }

    @Test
    void alreadyMemberExistTest(){

        //scenario 1
        when(memberRepository.findById(dummyMemberRegisterDTOList.get(1).getMemberId()))
                .thenReturn(Optional.of(generator.nextObject(Member.class)));

        boolean testToBeTrue1 = memberService.alreadyMemberExist(dummyMemberRegisterDTOList.get(1));

        Assertions.assertEquals(true, testToBeTrue1, "ok");


        //scenario 2
        when(memberRepository.findById(dummyMemberRegisterDTOList.get(2).getMemberId())).thenReturn(Optional.empty());

        boolean testToBeFalse1 = memberService.alreadyMemberExist(dummyMemberRegisterDTOList.get(2));

        Assertions.assertEquals(false, testToBeFalse1, "ok");
    }

    @Test
    void authenticateTest(){

        //scenario 1
        Member member = Member.builder()
                .memberId(dummyMemberAuthDTOList.get(1).getMemberId())
                .name("name")
                .password(dummyMemberAuthDTOList.get(1).getPassword())
                .build();

        when(memberRepository.findById(dummyMemberAuthDTOList.get(1).getMemberId()))
                .thenReturn(Optional.of(member));

        MemberIdNameDTO memberIdNameDTO = memberService.authenticate(dummyMemberAuthDTOList.get(1));

        Assertions.assertEquals(dummyMemberAuthDTOList.get(1).getMemberId(),memberIdNameDTO.getMemberId());
        Assertions.assertEquals("name",memberIdNameDTO.getName());


        //scenario 2
        when(memberRepository.findById(dummyMemberAuthDTOList.get(2).getMemberId())).thenReturn(Optional.empty());

        MemberIdNameDTO testToBeNull1 = memberService.authenticate(dummyMemberAuthDTOList.get(2));

        Assertions.assertEquals(null, testToBeNull1, "ok");
    }
}
