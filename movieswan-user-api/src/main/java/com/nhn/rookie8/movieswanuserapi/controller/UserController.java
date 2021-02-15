package com.nhn.rookie8.movieswanuserapi.controller;


import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.lib.CommonResponse;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.regex.*;


@RestController
@Log4j2
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public CommonResponse register(HttpServletRequest request) {

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

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Register success.")
                .build();

        return commonResponse;
    }

    @PostMapping("/login")
    public CommonResponse login(HttpServletRequest request){

        if(request.getSession(false) != null) {
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Already session exist.")
                    .build();
            return commonResponse;
        }

        String uid = request.getParameter("uid");
        String password = request.getParameter("password");

        UserDTO userDTO = userService.getUserInfoById(uid);

        if(userDTO == null){
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Incorrect ID.")
                    .content(1)
                    .build();
            return commonResponse;
        }

        if(!userDTO.getPassword().equals(password)){
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Incorrect password.")
                    .content(2)
                    .build();
            return commonResponse;
        }

        HttpSession session = request.getSession();
        session.setAttribute("uid",uid);

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Login Successfully.")
                .build();

        return commonResponse;

    }

    @GetMapping("/getUserInfo")
    public CommonResponse getUserInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null) {
            System.out.println("no session");

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(401)
                    .error(true)
                    .message("Login required.")
                    .build();

            return commonResponse;
        }

        String uid = (String) session.getAttribute("uid");

        UserDTO userDTO = userService.getUserInfoById(uid);
        UserDTO result = UserDTO.builder()
                .uid(userDTO.getUid())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .url(userDTO.getUrl())
                .build();

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("success")
                .content(result)
                .build();

        return commonResponse;
    }

    @PutMapping("/updateUserInfo")
    public CommonResponse updateUserInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null) {

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(401)
                    .error(true)
                    .message("Login required.")
                    .build();

            return commonResponse;
        }

        String uid = (String) session.getAttribute("uid");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String url = request.getParameter("url");

        UserDTO userDTO = userService.getUserInfoById(uid);

        UserDTO result = UserDTO.builder()
                .uid(userDTO.getUid())
                .password(userDTO.getPassword())
                .name(name)
                .email(email)
                .url(url)
                .regDate(userDTO.getRegDate())
                .modDate(LocalDateTime.now())
                .build();

        userService.update(result);

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Update success.")
                .build();

        return commonResponse;
    }

    @DeleteMapping("/deleteUser")
    public CommonResponse deleteUser(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null) {

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(401)
                    .error(true)
                    .message("Login required.")
                    .build();

            return commonResponse;
        }

        String uid = request.getParameter("uid");

        session.invalidate();

        userService.deleteById(uid);


        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Delete success.")
                .build();

        return commonResponse;

    }

    @PostMapping("/checkPassword")
    public CommonResponse checkPassword(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session == null) {

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(401)
                    .error(true)
                    .message("Login required.")
                    .build();

            return commonResponse;
        }

        String uid = (String) session.getAttribute("uid");

        String password = request.getParameter("password");

        UserDTO userDTO = userService.getUserInfoById(uid);

        if(userDTO == null){
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Unexpected error.")
                    .build();

            return commonResponse;
        }

        if(!userDTO.getPassword().equals(password)){

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Incorrect password.")
                    .content(2)
                    .build();

            return commonResponse;
        }

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Check Success.")
                .build();

        return commonResponse;

    }

    @PutMapping("/updatePassword")
    public CommonResponse updatePassword(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null) {

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(401)
                    .error(true)
                    .message("Login required.")
                    .build();

            return commonResponse;
        }

        String uid = (String) session.getAttribute("uid");

        String password = request.getParameter("password");

        UserDTO userDTO = userService.getUserInfoById(uid);

        UserDTO result = UserDTO.builder()
                .uid(userDTO.getUid())
                .password(password)
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .url(userDTO.getUrl())
                .regDate(userDTO.getRegDate())
                .modDate(LocalDateTime.now())
                .build();


        userService.register(result);

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Update password success.")
                .build();

        return commonResponse;
    }

    @GetMapping("/logout")
    public CommonResponse logout(HttpServletRequest request){

        HttpSession session = request.getSession();

        if(session == null) {

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(401)
                    .error(true)
                    .message("Login required.")
                    .build();

            return commonResponse;
        }

        session.invalidate();

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Logout success.")
                .build();

        return commonResponse;

    }

    @PostMapping("/isLoggedIn")
    public CommonResponse isLoggedIn(HttpServletRequest request){

        HttpSession session = request.getSession();

        if(session == null) {

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(401)
                    .error(true)
                    .message("Login required.")
                    .build();

            return commonResponse;
        }

        String uid = (String) session.getAttribute("uid");

        UserDTO result = UserDTO.builder()
                .uid(uid)
                .build();

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Session exist")
                .content(result)
                .build();

        return commonResponse;

    }

    @GetMapping("/isValidId")
    public CommonResponse isValidId(@RequestParam String uid){

        //Need to strict
        Pattern pattern = Pattern.compile("(^[a-z0-9._-]{6,20}$)");

        Matcher matcher = pattern.matcher(uid);
        if(matcher.find()){
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(200)
                    .error(false)
                    .message("Valid ID.")
                    .content(uid)
                    .build();

            return commonResponse;

        }

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(400)
                .error(true)
                .message("Invalid ID.")
                .content(uid)
                .build();

        return commonResponse;

    }
}
