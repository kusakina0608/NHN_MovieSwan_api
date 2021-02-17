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

        log.info("/api/register started..........");

        String uid = request.getParameter("uid");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String url = request.getParameter("url");

        UserDTO check = userService.getUserInfoById(uid);

        if(check != null){
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(false)
                    .message("Id already exist.")
                    .build();

            log.info("Id already exist.");
            log.info("uid : " + uid);
            log.info("/api/register end..........");

            return commonResponse;
        }

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

        log.info("Register success.");
        log.info("uid : " + uid);
        log.info("/api/register end..........");

        return commonResponse;
    }

    @PostMapping("/login")
    public CommonResponse login(HttpServletRequest request){

        log.info("/api/login start..........");

        if(request.getSession(false) != null) {
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Already session exist.")
                    .build();

            log.info("Already session exist.");
            log.info("/api/login end..........");

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

            log.info("Incorrect ID.    uid : " + uid);
            log.info("/api/login end..........");
            return commonResponse;
        }

        if(!userDTO.getPassword().equals(password)){
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Incorrect password.")
                    .content(2)
                    .build();

            log.info("Incorrect password.");
            log.info("uid : " + uid);
            log.info("/api/login end..........");
            return commonResponse;
        }


        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Login Successfully.")
                .build();

        log.info("Login Successfully.");
        log.info("uid : " + uid);
        log.info("/api/login end..........");

        return commonResponse;

    }

    @GetMapping("/getUserInfo")
    public CommonResponse getUserInfo(HttpServletRequest request) {

        log.info("/api/getUserInfo start..........");

        String uid = request.getParameter("uid");

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

        log.info("success.");
        log.info("uid : " + uid);
        log.info("/api/getUserInfo end..........");

        return commonResponse;
    }

    @PutMapping("/updateUserInfo")
    public CommonResponse updateUserInfo(HttpServletRequest request) {

        log.info("/api/updateUserInfo start..........");


        String uid = request.getParameter("uid");
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

        log.info("Update success.");
        log.info("uid : " + uid);
        log.info("/api/updateUserInfo end..........");

        return commonResponse;
    }

    @DeleteMapping("/deleteUser")
    public CommonResponse deleteUser(HttpServletRequest request) {

        log.info("/api/deleteUser start..........");


        String uid = request.getParameter("uid");

        userService.deleteById(uid);


        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Delete success.")
                .build();

        log.info("Delete success.");
        log.info("uid : " + uid);
        log.info("/api/deleteUser end..........");

        return commonResponse;

    }

    @PostMapping("/checkPassword")
    public CommonResponse checkPassword(HttpServletRequest request){

        log.info("/api/checkPassword start..........");


        String uid = request.getParameter("uid");
        String password = request.getParameter("password");

        UserDTO userDTO = userService.getUserInfoById(uid);

        if(userDTO == null){
            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Unexpected error.")
                    .build();

            log.info("Unexpected error");
            log.info("/api/checkPassword end..........");

            return commonResponse;
        }

        if(!userDTO.getPassword().equals(password)){

            CommonResponse commonResponse = CommonResponse.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Incorrect password.")
                    .content(2)
                    .build();

            log.info("Incorrect password.");
            log.info("uid : " + uid);
            log.info("/api/checkPassword end..........");

            return commonResponse;
        }

        CommonResponse commonResponse = CommonResponse.builder()
                .httpCode(200)
                .error(false)
                .message("Check Success.")
                .build();

        log.info("Check Success.");
        log.info("uid : " + uid);
        log.info("/api/checkPassword end..........");

        return commonResponse;

    }

    @PutMapping("/updatePassword")
    public CommonResponse updatePassword(HttpServletRequest request) {

        log.info("/api/updatePassword start..........");


        String uid = request.getParameter("uid");
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

        log.info("Update password success.");
        log.info("uid : " + uid);
        log.info("/api/updatePassword end..........");

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
