package com.nhn.rookie8.movieswanuserapi.controller;


import com.nhn.rookie8.movieswanuserapi.dto.*;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import com.nhn.rookie8.movieswanuserapi.userexception.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseDTO register(@RequestBody UserBasicDTO request) {

        if(!userService.check(request)){
            throw new InputErrorException();
        }

        if(userService.alreadyUserExist(request)){
            throw new IdOrPasswordErrorException();
        }

        userService.register(request);

        return userService.responseWithoutContent(ErrorCode.NO_ERROR);
    }


    @PostMapping("/auth")
    public ResponseDTO auth(@RequestBody UserAuthDTO request) {

        if(!userService.check(request)){
            throw new InputErrorException();
        }

        if(!userService.authenticate(request)){
            throw new IdOrPasswordErrorException();
        }

        return userService.responseWithoutContent(ErrorCode.NO_ERROR);
    }


    @PostMapping("/getUserInfo")
    public ResponseDTO getUserInfo(@RequestBody UserIdDTO request) {

        if(!userService.check(request)){
            throw new InputErrorException();
        }

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IdOrPasswordErrorException();
        }

        return userService.responseWithContent(ErrorCode.NO_ERROR, userDTO);
    }



    @GetMapping("/isExistId")
    public ResponseDTO isExistId(@RequestParam String uid) {

        if(userService.checkString(uid)){
            throw new InputErrorException();
        }

        if(userService.getUserInfoById(uid) != null){
            throw new IdOrPasswordErrorException();
        }

        return userService.responseWithoutContent(ErrorCode.NO_ERROR);
    }

}

