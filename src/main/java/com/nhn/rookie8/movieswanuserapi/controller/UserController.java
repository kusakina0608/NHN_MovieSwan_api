package com.nhn.rookie8.movieswanuserapi.controller;


import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import com.nhn.rookie8.movieswanuserapi.userexception.*;

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


    @PostMapping("/register")
    public ResponseDTO register(@RequestBody UserDTO request) throws UserException{


        if(request == null || request.getUid().isEmpty()){
            throw new UnexpectedErrorException();
        }

        UserDTO check = userService.getUserInfoById(request.getUid());

        if(check != null){
            throw new AlreadyIdExistException();
        }

        request.setRegDate(LocalDateTime.now());
        request.setModDate(LocalDateTime.now());

        userService.register(request);

        ResponseDTO responseDTO= userService.returnResponseDto(ErrorCode.NO_ERROR, null);
        return responseDTO;
    }


    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserDTO request) throws UserException{

        if(request == null || request.getUid().isEmpty()){
            throw new UnexpectedErrorException();
        }

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        if(!userDTO.getPassword().equals(request.getPassword())){
            throw new IncorrectPasswordException();
        }


        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @PostMapping("/getUserInfo")
    public ResponseDTO getUserInfo(@RequestBody UserDTO request) throws UserException {

        if(request == null || request.getUid().isEmpty()){
            throw new UnexpectedErrorException();
        }

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        userDTO.setPassword(null);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, userDTO);
    }


    @PutMapping("/updateUserInfo")
    public ResponseDTO updateUserInfo(@RequestBody UserDTO request) throws UserException {

        if(request == null || request.getUid().isEmpty()){
            throw new UnexpectedErrorException();
        }

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        userDTO.setName(request.getName());
        userDTO.setEmail(request.getEmail());
        userDTO.setUrl(request.getUrl());
        userDTO.setModDate(LocalDateTime.now());

        userService.update(userDTO);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @DeleteMapping("/deleteUser")
    public ResponseDTO deleteUser(@RequestBody UserDTO request) throws UserException {

        if(request == null || request.getUid().isEmpty()){
            throw new UnexpectedErrorException();
        }

        userService.deleteById(request.getUid());

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @PostMapping("/checkPassword")
    public ResponseDTO checkPassword(@RequestBody UserDTO request) throws UserException {

        if(request == null || request.getUid().isEmpty()){
            throw new UnexpectedErrorException();
        }

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        if(!userDTO.getPassword().equals(request.getPassword())){
            throw new IncorrectPasswordException();
        }


        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @PutMapping("/updatePassword")
    public ResponseDTO updatePassword(@RequestBody UserDTO request) throws UserException {

        if(request == null || request.getUid().isEmpty()){
            throw new UnexpectedErrorException();
        }

        UserDTO userDTO = userService.getUserInfoById(request.getUid());

        if(userDTO == null){
            throw new IncorrectUserException();
        }

        userDTO.setPassword(request.getPassword());
        userDTO.setModDate(LocalDateTime.now());

        userService.register(userDTO);

        return userService.returnResponseDto(ErrorCode.NO_ERROR, null);
    }


    @GetMapping("/isValidId")
    public ResponseDTO isValidId(@RequestParam String uid) throws UserException{

        if(uid.isEmpty()){
            throw new UnexpectedErrorException();
        }

        if(userService.getUserInfoById(uid) != null){
            throw new AlreadyIdExistException();
        }

        return userService.returnResponseDto(ErrorCode.NO_ERROR, UserDTO.builder().uid(uid).build());
    }

}

