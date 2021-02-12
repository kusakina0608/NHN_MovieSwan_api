package com.nhn.rookie8.movieswanuserapi.controller;


import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;


@RestController
@Log4j2
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public void register(HttpServletRequest request) {

        String uid = request.getParameter("uid");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String url = request.getParameter("url");


        UserDTO userDTO = UserDTO.builder()
                .uid(uid)
                .password(password)
                .name(name)
                .email(email)
                .url(url)
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .build();

        userService.register(userDTO);

    }

    @PostMapping("/login")
    public String login(HttpServletRequest request){

        if(request.getSession(false) != null) {
            System.out.println("already session exist");
            return null;
        }

        String uid = request.getParameter("uid");
        String password = request.getParameter("password");

        UserDTO userDTO = userService.getUserInfoById(uid);

        if(userDTO == null){
            return "{\"error\":\"true\",\"errorCode\":1,\"message\":\"Incorrect Id\"}";
        }

        if(!userDTO.getPassword().equals(password)){
            return "{\"error\":\"true\",\"errorCode\":2,\"message\":\"Incorrect Password\"}";
        }

        HttpSession session = request.getSession();
        session.setAttribute("uid",uid);

        return "{\"error\":\"false\",\"errorCode\":,\"message\":\"Login Successfully\"}";
    }

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null) {
            System.out.println("no session");
            return null;
        }

        String uid = (String) session.getAttribute("uid");

        UserDTO userDTO = userService.getUserInfoById(uid);
        UserDTO result = UserDTO.builder()
                .uid(userDTO.getUid())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .url(userDTO.getUrl())
                .build();

        return result;
    }
}
