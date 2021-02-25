package com.nhn.rookie8.movieswanuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanuserapi.UserControllerAdvice;
import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.repository.UserRepository;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import com.nhn.rookie8.movieswanuserapi.service.UserServiceImpl;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Log4j2
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(UserController.class)
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

    private String createUid() {
        String text = "0123456789abcdefghijklmnopqrstuvwxyz._-";
        StringBuilder uid = new StringBuilder();
        int uidLength = (int)(Math.random()*20+1);

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
        System.out.println(uid);
    }

    @AfterEach
    public void tearDown(){
        System.out.println("teardown");
    }

    ResponseDTO responseDTO;
    @Test
    public void registerTest() throws Exception {

        UserDTO userDTO = UserDTO.builder().build();
        ResponseDTO responseDTO = ResponseDTO.builder().build();

        Mockito.when(userService.getUserInfoById(Mockito.anyString())).thenReturn(userDTO);
        Mockito.when(userService.register(Mockito.any(UserDTO.class))).thenReturn(0L);
        Mockito.when(userService.returnResponseDto(Mockito.any(ErrorCode.class), Mockito.any(UserDTO.class))).thenReturn(responseDTO);

        String content = objectMapper.writeValueAsString(UserDTO.builder().uid(uid).build());
        System.out.println(content);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/getUserInfo")
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andDo(print()).andReturn();

        System.out.println("#$^$#%");
        System.out.println(result.getResponse().getContentAsString());

        System.out.println("123@#$");
    }
}
