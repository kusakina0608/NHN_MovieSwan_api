package com.nhn.rookie8.movieswanuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanuserapi.UserControllerAdvice;
import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.repository.UserRepository;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import com.nhn.rookie8.movieswanuserapi.service.UserServiceImpl;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import com.nhn.rookie8.movieswanuserapi.userexception.UnexpectedErrorException;
import com.nhn.rookie8.movieswanuserapi.userexception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Log4j2
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest({UserController.class,UserControllerAdvice.class})
public class UserControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private String uid;


    private List<ResponseDTO> responseDTOList;

    private UserDTO userDTO;

    private String createUid() {
        String text = "0123456789abcdefghijklmnopqrstuvwxyz._-";
        StringBuilder uid = new StringBuilder();
        int uidLength = (int)(Math.random()*20) + 1;

        IntStream.rangeClosed(1,uidLength).forEach(i->{
            int idx = (int)(Math.random()*text.length());
            uid.append(text.charAt(idx));
        });

        return uid.toString();
    }

    @BeforeEach
    public void setUp(){
        System.out.println("setup");
        uid = createUid();

        userDTO = UserDTO.builder()
                .uid(uid)
                .password("asdf")
                .name("asdf")
                .email("asdf")
                .url("asdf")
                .build();

        responseDTOList = new ArrayList<>();

        IntStream.rangeClosed(0,4).forEach(i->{
            responseDTOList.add(ResponseDTO.builder()
                    .httpCode(i==0?200:400)
                    .error(i==0)
                    .errorCode(i)
                    .message(ErrorCode.values()[i].getMessage())
                    .build());
        });

    }

    @AfterEach
    public void tearDown(){
        System.out.println("teardown");
    }

    @Test
    public void registerTest() throws Exception {

        //존재하는 ID이면 회원가입 미진행
        when(userService.getUserInfoById("uid"))
                .thenReturn(userDTO);

        //존재하지 않는 ID이면 회원가입 진행
        when(userService.getUserInfoById(userDTO.getUid()))
                .thenReturn(null);

        when(userService.register(userDTO))
                .thenReturn(null);

        when(userService.returnResponseDto(ErrorCode.NO_ERROR, null))
                .thenReturn(responseDTOList.get(0));

        //가입가능
        String content1 = objectMapper.writeValueAsString(userDTO);

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .post("/api/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(content1)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result1 = mockMvc.perform(requestBuilder1).andDo(print()).andReturn();

        ResponseDTO res = objectMapper.readValue(result1.getResponse().getContentAsString(), ResponseDTO.class);

        Assertions.assertEquals(responseDTOList.get(0), res, "ok");

        //가입불가
        String content2 = objectMapper.writeValueAsString(UserDTO.builder().uid("uid").build());

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/api/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(content2)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result2 = mockMvc.perform(requestBuilder2).andDo(print()).andReturn();

    }

    @Test
    public void loginTest() throws Exception {

        //Id, password 모두 일치
        when(userService.getUserInfoById(userDTO.getUid()))
                .thenReturn(userDTO);

        //Id 불일치
        when(userService.getUserInfoById("uid"))
                .thenReturn(null);


        //TODO: pw 불일치


        when(userService.returnResponseDto(ErrorCode.NO_ERROR, null))
                .thenReturn(responseDTOList.get(0));

        //로그인성공
        String content1 = objectMapper.writeValueAsString(userDTO);

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .post("/api/login")
                .accept(MediaType.APPLICATION_JSON)
                .content(content1)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result1 = mockMvc.perform(requestBuilder1).andDo(print()).andReturn();

        ResponseDTO res = objectMapper.readValue(result1.getResponse().getContentAsString(), ResponseDTO.class);

        Assertions.assertEquals(responseDTOList.get(0), res, "ok");

        //로그인실패
        String content2 = objectMapper.writeValueAsString(UserDTO.builder().uid("uid").build());

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/api/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(content2)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result2 = mockMvc.perform(requestBuilder2).andDo(print()).andReturn();

    }

    @Test
    public void getUserInfoTest() throws Exception {

        when(userService.getUserInfoById(any(String.class)))
                .thenReturn(userDTO);


        when(userService.returnResponseDto(Mockito.any(ErrorCode.class), Mockito.any(UserDTO.class)))
                .thenReturn(responseDTOList.get(0));

        String content = objectMapper.writeValueAsString(UserDTO.builder().uid(uid).build());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/getUserInfo")
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andDo(print()).andReturn();
        ResponseDTO res = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseDTO.class);

        Assertions.assertEquals(responseDTOList.get(0), res, "ok");

    }
}
