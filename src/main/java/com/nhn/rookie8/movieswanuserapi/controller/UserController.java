package com.nhn.rookie8.movieswanuserapi.controller;


import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import com.nhn.rookie8.movieswanuserapi.userexception.AlreadyIdExistException;
import com.nhn.rookie8.movieswanuserapi.userexception.IncorrectPasswordException;
import com.nhn.rookie8.movieswanuserapi.userexception.IncorrectUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;



@RequiredArgsConstructor
@RestController
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    private String divideLog = "--------------------------------------------------";
    private String startMessage = "started..........";
    private String endMessage = "end..........";

    @PostMapping("/register")
    public ResponseDTO register(@RequestBody UserDTO request) throws Exception{

        log.info(divideLog);
        log.info("/api/register {}",startMessage);

        UserDTO check = userService.getUserInfoById(request.getUid());

        if(check != null){
            throw new AlreadyIdExistException();
        }

        request.setRegDate(LocalDateTime.now());
        request.setModDate(LocalDateTime.now());

        userService.register(request);

        log.info("uid : {}", request.getUid());
        log.info("/api/register {}",endMessage);
        log.info(divideLog);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserDTO request) throws Exception{

        log.info(divideLog);
        log.info("/api/login {}",startMessage);
        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        if(!userDTO.getPassword().equals(request.getPassword())){
            throw new IncorrectPasswordException();
        }

        log.info("uid : {}", request.getUid());
        log.info("/api/login {}", endMessage);
        log.info(divideLog);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @PostMapping("/getUserInfo")
    public ResponseDTO getUserInfo(@RequestBody UserDTO request) throws Exception {

        log.info(divideLog);
        log.info("/api/getUserInfo {}",startMessage);

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        userDTO.setPassword(null);

        log.info("uid : {}", request.getUid());
        log.info("/api/getUserInfo {}", endMessage);
        log.info(divideLog);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, userDTO);
    }


    @PutMapping("/updateUserInfo")
    public ResponseDTO updateUserInfo(@RequestBody UserDTO request) throws Exception {

        log.info(divideLog);
        log.info("/api/updateUserInfo {}", startMessage);

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        userDTO.setName(request.getName());
        userDTO.setEmail(request.getEmail());
        userDTO.setUrl(request.getUrl());
        userDTO.setModDate(LocalDateTime.now());

        userService.update(userDTO);

        log.info("uid : {}", request.getUid());
        log.info("/api/updateUserInfo {}", endMessage);
        log.info(divideLog);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @DeleteMapping("/deleteUser")
    public ResponseDTO deleteUser(@RequestBody UserDTO request) throws Exception {

        log.info(divideLog);
        log.info("/api/deleteUser {}", startMessage);

        userService.deleteById(request.getUid());

        log.info("uid : {}", request.getUid());
        log.info("/api/deleteUser {}", endMessage);
        log.info(divideLog);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @PostMapping("/checkPassword")
    public ResponseDTO checkPassword(@RequestBody UserDTO request) throws Exception {

        log.info(divideLog);
        log.info("/api/checkPassword {}", startMessage);

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        if(!userDTO.getPassword().equals(request.getPassword())){
            throw new IncorrectPasswordException();
        }

        log.info("uid : {}", request.getUid());
        log.info("/api/checkPassword {}", endMessage);
        log.info(divideLog);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @PutMapping("/updatePassword")
    public ResponseDTO updatePassword(@RequestBody UserDTO request) throws Exception {

        log.info(divideLog);
        log.info("/api/updatePassword {}", startMessage);

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        userDTO.setPassword(request.getPassword());
        userDTO.setModDate(LocalDateTime.now());

        userService.register(userDTO);

        log.info("uid : {}", request.getUid());
        log.info("/api/updatePassword {}", endMessage);
        log.info(divideLog);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @GetMapping("/isValidId")
    public ResponseDTO isValidId(@RequestParam String uid) throws Exception{
        if(userService.getUserInfoById(uid) != null){
            throw new AlreadyIdExistException();
        }
        return userService.returnResponseDto(ErrorCode.NO_ERROR, UserDTO.builder().uid(uid).build());
    }

}

