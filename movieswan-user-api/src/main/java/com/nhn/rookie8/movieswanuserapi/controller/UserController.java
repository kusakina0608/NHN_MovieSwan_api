package com.nhn.rookie8.movieswanuserapi.controller;


import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.regex.*;


@RestController
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @PostMapping("/register")
    public ResponseDTO register(@RequestBody UserDTO request) {

        log.info("--------------------------------------------------");
        log.info("/api/register started..........");

        UserDTO check = userService.getUserInfoById(request.getUid());

        if(check != null){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .httpCode(400)
                    .error(false)
                    .message("Id already exist.")
                    .build();

            log.info("Id already exist.");
            log.info("uid : " + request.getUid());
            log.info("/api/register end..........");
            log.info("--------------------------------------------------");

            return responseDTO;
        }

        request.setRegDate(LocalDateTime.now());
        request.setModDate(LocalDateTime.now());

        userService.register(request);

        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpCode(200)
                .error(false)
                .message("Register success.")
                .build();

        log.info("Register success.");
        log.info("uid : " + request.getUid());
        log.info("/api/register end..........");
        log.info("--------------------------------------------------");

        return responseDTO;
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserDTO request){

        log.info("--------------------------------------------------");
        log.info("/api/login start..........");

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Incorrect ID.")
                    .content(1)
                    .build();

            log.info("Incorrect ID.    uid : " + request.getUid());
            log.info("/api/login end..........");
            log.info("--------------------------------------------------");

            return responseDTO;
        }

        if(!userDTO.getPassword().equals(request.getPassword())){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Incorrect password.")
                    .content(2)
                    .build();

            log.info("Incorrect password.");
            log.info("uid : " + request.getUid());
            log.info("/api/login end..........");
            log.info("--------------------------------------------------");

            return responseDTO;
        }


        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpCode(200)
                .error(false)
                .message("Login Successfully.")
                .build();

        log.info("Login Successfully.");
        log.info("uid : " + request.getUid());
        log.info("/api/login end..........");
        log.info("--------------------------------------------------");

        return responseDTO;

    }

    @ResponseBody
    @PostMapping("/getUserInfo")
    public ResponseDTO getUserInfo(@RequestBody UserDTO request) {

        log.info("--------------------------------------------------");
        log.info("/api/getUserInfo start..........");

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpCode(200)
                .error(false)
                .message("success")
                .content(userDTO)
                .build();

        log.info("success.");
        log.info("uid : " + request.getUid());
        log.info("/api/getUserInfo end..........");
        log.info("--------------------------------------------------");

        return responseDTO;
    }

    @ResponseBody
    @PutMapping("/updateUserInfo")
    public ResponseDTO updateUserInfo(@RequestBody UserDTO request) {

        log.info("--------------------------------------------------");
        log.info("/api/updateUserInfo start..........");

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        userDTO.setName(request.getName());
        userDTO.setEmail(request.getEmail());
        userDTO.setUrl(request.getUrl());

        userService.update(userDTO);

        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpCode(200)
                .error(false)
                .message("Update success.")
                .build();

        log.info("Update success.");
        log.info("uid : " + request.getUid());
        log.info("/api/updateUserInfo end..........");
        log.info("--------------------------------------------------");

        return responseDTO;
    }

    @ResponseBody
    @DeleteMapping("/deleteUser")
    public ResponseDTO deleteUser(@RequestBody UserDTO request) {

        log.info("--------------------------------------------------");
        log.info("/api/deleteUser start..........");

        userService.deleteById(request.getUid());


        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpCode(200)
                .error(false)
                .message("Delete success.")
                .build();

        log.info("Delete success.");
        log.info("uid : " + request.getUid());
        log.info("/api/deleteUser end..........");
        log.info("--------------------------------------------------");

        return responseDTO;

    }

    @ResponseBody
    @PostMapping("/checkPassword")
    public ResponseDTO checkPassword(@RequestBody UserDTO request){

        log.info("--------------------------------------------------");
        log.info("/api/checkPassword start..........");


        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Unexpected error.")
                    .build();

            log.info("Unexpected error");
            log.info("/api/checkPassword end..........");
            log.info("--------------------------------------------------");

            return responseDTO;
        }

        if(!userDTO.getPassword().equals(request.getPassword())){

            ResponseDTO responseDTO = ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Incorrect password.")
                    .content(2)
                    .build();

            log.info("Incorrect password.");
            log.info("uid : " + request.getUid());
            log.info("/api/checkPassword end..........");
            log.info("--------------------------------------------------");

            return responseDTO;
        }

        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpCode(200)
                .error(false)
                .message("Check Success.")
                .build();

        log.info("Check Success.");
        log.info("uid : " + request.getUid());
        log.info("/api/checkPassword end..........");
        log.info("--------------------------------------------------");

        return responseDTO;

    }

    @ResponseBody
    @PutMapping("/updatePassword")
    public ResponseDTO updatePassword(@RequestBody UserDTO request) {

        log.info("--------------------------------------------------");
        log.info("/api/updatePassword start..........");


        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .message("Unexpected error.")
                    .build();

            log.info("Unexpected error");
            log.info("/api/checkPassword end..........");
            log.info("--------------------------------------------------");

            return responseDTO;
        }

        userDTO.setPassword(request.getPassword());


        userService.register(userDTO);

        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpCode(200)
                .error(false)
                .message("Update password success.")
                .build();

        log.info("Update password success.");
        log.info("uid : " + request.getUid());
        log.info("/api/updatePassword end..........");
        log.info("--------------------------------------------------");

        return responseDTO;
    }


    @GetMapping("/isValidId")
    public ResponseDTO isValidId(@RequestParam String uid){

        //Need to strict
        Pattern pattern = Pattern.compile("(^[a-z0-9._-]{6,20}$)");

        Matcher matcher = pattern.matcher(uid);
        if(matcher.find()){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .message("Valid ID.")
                    .content(uid)
                    .build();

            return responseDTO;

        }

        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpCode(400)
                .error(true)
                .message("Invalid ID.")
                .content(uid)
                .build();

        return responseDTO;

    }

}
