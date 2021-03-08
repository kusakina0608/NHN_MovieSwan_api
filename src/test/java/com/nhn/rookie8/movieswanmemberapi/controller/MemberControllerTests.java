package com.nhn.rookie8.movieswanmemberapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanmemberapi.dto.*;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import com.nhn.rookie8.movieswanmemberapi.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Log4j2
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(MemberController.class)
class MemberControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    EasyRandom generator;

    @MockBean
    private MemberService memberService;

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
    void registerTest() throws Exception {

        //scenario 1
        when(memberService.checkInput(dummyMemberRegisterDTOList.get(1))).thenReturn(false);

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .post("/api/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberRegisterDTOList.get(1)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult1 = mockMvc.perform(requestBuilder1).andDo(print()).andReturn();

        Assertions.assertEquals("", mvcResult1.getResponse().getContentAsString(), "ok");


        //scenario 2
        when(memberService.checkInput(dummyMemberRegisterDTOList.get(2))).thenReturn(true);
        when(memberService.alreadyMemberExist(dummyMemberRegisterDTOList.get(2))).thenReturn(true);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/api/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberRegisterDTOList.get(2)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult2 = mockMvc.perform(requestBuilder2).andDo(print()).andReturn();

        Assertions.assertEquals("", mvcResult2.getResponse().getContentAsString(), "ok");


        //scenario 3
        when(memberService.checkInput(dummyMemberRegisterDTOList.get(3))).thenReturn(true);
        when(memberService.alreadyMemberExist(dummyMemberRegisterDTOList.get(3))).thenReturn(false);
        when(memberService.responseWithoutContent(ErrorCode.NO_ERROR)).thenReturn(dummyResponseDTOList.get(3));

        RequestBuilder requestBuilder3 = MockMvcRequestBuilders
                .post("/api/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberRegisterDTOList.get(3)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult3 = mockMvc.perform(requestBuilder3).andDo(print()).andReturn();

        ResponseDTO responseDTO3 =
                objectMapper.readValue(mvcResult3.getResponse().getContentAsString(), ResponseDTO.class);

        Assertions.assertEquals(dummyResponseDTOList.get(3), responseDTO3, "ok");
    }

    @Test
    void authTest() throws Exception {

        //scenario 1
        when(memberService.checkInput(dummyMemberAuthDTOList.get(1))).thenReturn(false);

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .post("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberAuthDTOList.get(1)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult1 = mockMvc.perform(requestBuilder1).andDo(print()).andReturn();

        Assertions.assertEquals("", mvcResult1.getResponse().getContentAsString(), "ok");


        //scenario 2
        when(memberService.checkInput(dummyMemberAuthDTOList.get(2))).thenReturn(true);
        when(memberService.authenticate(dummyMemberAuthDTOList.get(2))).thenReturn(null);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberAuthDTOList.get(2)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult2 = mockMvc.perform(requestBuilder2).andDo(print()).andReturn();

        Assertions.assertEquals("", mvcResult2.getResponse().getContentAsString(), "ok");


        //scenario 3
        when(memberService.checkInput(dummyMemberAuthDTOList.get(3))).thenReturn(true);
        when(memberService.authenticate(dummyMemberAuthDTOList.get(3))).thenReturn(dummyMemberIdNameDTOList.get(3));
        when(memberService.responseWithContent(ErrorCode.NO_ERROR,dummyMemberIdNameDTOList.get(3)))
                .thenReturn(dummyResponseDTOList.get(3));

        RequestBuilder requestBuilder3 = MockMvcRequestBuilders
                .post("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberAuthDTOList.get(3)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult3 = mockMvc.perform(requestBuilder3).andDo(print()).andReturn();

        ResponseDTO responseDTO3 =
                objectMapper.readValue(mvcResult3.getResponse().getContentAsString(), ResponseDTO.class);

        Assertions.assertEquals(dummyResponseDTOList.get(3), responseDTO3, "ok");
    }

    @Test
    void getMemberInfoTest() throws Exception {

        //scenario 1
        when(memberService.checkInput(dummyMemberIdDTOList.get(1))).thenReturn(false);

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .post("/api/getMemberInfo")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberIdDTOList.get(1)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult1 = mockMvc.perform(requestBuilder1).andDo(print()).andReturn();

        Assertions.assertEquals("", mvcResult1.getResponse().getContentAsString(), "ok");


        //scenario 2
        when(memberService.checkInput(dummyMemberIdDTOList.get(2))).thenReturn(true);
        when(memberService.getMemberInfoById(dummyMemberIdDTOList.get(2).getMemberId())).thenReturn(null);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/api/getMemberInfo")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberIdDTOList.get(2)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult2 = mockMvc.perform(requestBuilder2).andDo(print()).andReturn();

        Assertions.assertEquals("", mvcResult2.getResponse().getContentAsString(), "ok");


        //scenario 3
        when(memberService.checkInput(dummyMemberIdDTOList.get(3))).thenReturn(true);
        when(memberService.getMemberInfoById(dummyMemberIdDTOList.get(3).getMemberId()))
                .thenReturn(dummyMemberDTOList.get(3));
        when(memberService.responseWithContent(ErrorCode.NO_ERROR,dummyMemberDTOList.get(3)))
                .thenReturn(dummyResponseDTOList.get(3));

        RequestBuilder requestBuilder3 = MockMvcRequestBuilders
                .post("/api/getMemberInfo")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMemberIdDTOList.get(3)))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult3 = mockMvc.perform(requestBuilder3).andDo(print()).andReturn();

        ResponseDTO responseDTO3 =
                objectMapper.readValue(mvcResult3.getResponse().getContentAsString(), ResponseDTO.class);

        Assertions.assertEquals(dummyResponseDTOList.get(3), responseDTO3, "ok");

    }


    @Test
    void isExistIdTest() throws Exception {

        //scenario 1
        when(memberService.checkString(dummyMemberIdList.get(1))).thenReturn(false);

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .get("/api/isExistId/?memberId="+dummyMemberIdList.get(1));


        MvcResult mvcResult1 = mockMvc.perform(requestBuilder1).andDo(print()).andReturn();

        Assertions.assertEquals("", mvcResult1.getResponse().getContentAsString(), "ok");


        //scenario 2
        when(memberService.checkString(dummyMemberIdList.get(2))).thenReturn(true);
        when(memberService.getMemberInfoById(dummyMemberIdList.get(2))).thenReturn(dummyMemberDTOList.get(2));

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .get("/api/isExistId/?memberId="+dummyMemberIdList.get(2));

        MvcResult mvcResult2 = mockMvc.perform(requestBuilder2).andDo(print()).andReturn();

        Assertions.assertEquals("", mvcResult2.getResponse().getContentAsString(), "ok");


        //scenario 3
        when(memberService.checkString(dummyMemberIdList.get(3))).thenReturn(true);
        when(memberService.getMemberInfoById(dummyMemberIdList.get(3))).thenReturn(null);
        when(memberService.responseWithoutContent(ErrorCode.NO_ERROR))
                .thenReturn(dummyResponseDTOList.get(3));

        RequestBuilder requestBuilder3 = MockMvcRequestBuilders
                .get("/api/isExistId/?memberId="+dummyMemberIdList.get(3));

        MvcResult mvcResult3 = mockMvc.perform(requestBuilder3).andDo(print()).andReturn();

        ResponseDTO responseDTO3 =
                objectMapper.readValue(mvcResult3.getResponse().getContentAsString(), ResponseDTO.class);

        Assertions.assertEquals(dummyResponseDTOList.get(3), responseDTO3, "ok");
    }
}

