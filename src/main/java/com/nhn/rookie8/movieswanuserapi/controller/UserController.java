package com.nhn.rookie8.movieswanuserapi.controller;


import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.dto.UserDTO;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import com.nhn.rookie8.movieswanuserapi.userEnum.ErrorCode;
import com.nhn.rookie8.movieswanuserapi.userException.AlreadyIdExistException;
import com.nhn.rookie8.movieswanuserapi.userException.IncorrectPasswordException;
import com.nhn.rookie8.movieswanuserapi.userException.IncorrectUserException;
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
    public ResponseDTO register(@RequestBody UserDTO request) {

        try{

            log.info(divideLog);
            log.info("/api/register {}",startMessage);

            UserDTO check = userService.getUserInfoById(request.getUid());

            if(check != null){
                throw new AlreadyIdExistException();
            }

            request.setRegDate(LocalDateTime.now());
            request.setModDate(LocalDateTime.now());

            userService.register(request);
            log.info(ErrorCode.NO_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .errorCode(ErrorCode.NO_ERROR.ordinal())
                    .message(ErrorCode.NO_ERROR.getMessage())
                    .build();

        }
        catch (AlreadyIdExistException e){

            log.info(ErrorCode.ALREADY_ID_EXIST.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.ALREADY_ID_EXIST.ordinal())
                    .message(ErrorCode.ALREADY_ID_EXIST.getMessage())
                    .build();
        }
        catch (Exception e){

            log.info(ErrorCode.UNEXPECTED_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.UNEXPECTED_ERROR.ordinal())
                    .message(ErrorCode.UNEXPECTED_ERROR.getMessage())
                    .build();

        }
        finally {

            log.info("uid : {}", request.getUid());
            log.info("/api/register {}",endMessage);
            log.info(divideLog);

        }
    }


    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserDTO request){

        try{

            log.info(divideLog);
            log.info("/api/login {}",startMessage);
            UserDTO userDTO = userService.getUserInfoById(request.getUid());

            if(userDTO == null){
                throw new IncorrectUserException();
            }

            if(!userDTO.getPassword().equals(request.getPassword())){
                throw new IncorrectPasswordException();
            }

            log.info(ErrorCode.NO_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .errorCode(ErrorCode.NO_ERROR.ordinal())
                    .message(ErrorCode.NO_ERROR.getMessage())
                    .build();

        }
        catch (IncorrectUserException e){

            log.info(ErrorCode.INCORRECT_USER.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.INCORRECT_USER.ordinal())
                    .message(ErrorCode.INCORRECT_USER.getMessage())
                    .build();

        }
        catch (IncorrectPasswordException e){

            log.info(ErrorCode.INCORRECT_PASSWORD.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.INCORRECT_PASSWORD.ordinal())
                    .message(ErrorCode.INCORRECT_PASSWORD.getMessage())
                    .build();

        }
        catch (Exception e){

            log.info(ErrorCode.UNEXPECTED_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.UNEXPECTED_ERROR.ordinal())
                    .message(ErrorCode.UNEXPECTED_ERROR.getMessage())
                    .build();

        }
        finally {

            log.info("uid : {}", request.getUid());
            log.info("/api/login {}", endMessage);
            log.info(divideLog);

        }
    }


    @PostMapping("/getUserInfo")
    public ResponseDTO getUserInfo(@RequestBody UserDTO request) {

        try{

            log.info(divideLog);
            log.info("/api/getUserInfo {}",startMessage);

            UserDTO userDTO = userService.getUserInfoById(request.getUid());

            if(userDTO == null){
                throw new IncorrectUserException();
            }

            userDTO.setPassword(null);

            log.info(ErrorCode.NO_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .errorCode(ErrorCode.NO_ERROR.ordinal())
                    .message(ErrorCode.NO_ERROR.getMessage())
                    .content(userDTO)
                    .build();
        }
        catch (IncorrectUserException e){

            log.info(ErrorCode.INCORRECT_USER.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.INCORRECT_USER.ordinal())
                    .message(ErrorCode.INCORRECT_USER.getMessage())
                    .build();

        }
        catch (Exception e){

            log.info(ErrorCode.UNEXPECTED_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.UNEXPECTED_ERROR.ordinal())
                    .message(ErrorCode.UNEXPECTED_ERROR.getMessage())
                    .build();

        }
        finally {

            log.info("uid : {}", request.getUid());
            log.info("/api/getUserInfo {}", endMessage);
            log.info(divideLog);

        }
    }


    @PutMapping("/updateUserInfo")
    public ResponseDTO updateUserInfo(@RequestBody UserDTO request) {

        try{

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

            log.info(ErrorCode.NO_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .errorCode(ErrorCode.NO_ERROR.ordinal())
                    .message(ErrorCode.NO_ERROR.getMessage())
                    .build();
        }
        catch (IncorrectUserException e){

            log.info(ErrorCode.INCORRECT_USER.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.INCORRECT_USER.ordinal())
                    .message(ErrorCode.INCORRECT_USER.getMessage())
                    .build();

        }
        catch (Exception e){

            log.info(ErrorCode.UNEXPECTED_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.UNEXPECTED_ERROR.ordinal())
                    .message(ErrorCode.UNEXPECTED_ERROR.getMessage())
                    .build();

        }
        finally {

            log.info("uid : {}", request.getUid());
            log.info("/api/updateUserInfo {}", endMessage);
            log.info(divideLog);

        }
    }


    @DeleteMapping("/deleteUser")
    public ResponseDTO deleteUser(@RequestBody UserDTO request) {

        try{

            log.info(divideLog);
            log.info("/api/deleteUser {}", startMessage);

            userService.deleteById(request.getUid());

            log.info(ErrorCode.NO_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .errorCode(ErrorCode.NO_ERROR.ordinal())
                    .message(ErrorCode.NO_ERROR.getMessage())
                    .build();

        }
        catch (Exception e){

            log.info(ErrorCode.UNEXPECTED_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.UNEXPECTED_ERROR.ordinal())
                    .message(ErrorCode.UNEXPECTED_ERROR.getMessage())
                    .build();

        }
        finally {

            log.info("uid : {}", request.getUid());
            log.info("/api/deleteUser {}", endMessage);
            log.info(divideLog);

        }
    }


    @PostMapping("/checkPassword")
    public ResponseDTO checkPassword(@RequestBody UserDTO request){

        try{

            log.info(divideLog);
            log.info("/api/checkPassword {}", startMessage);

            UserDTO userDTO = userService.getUserInfoById(request.getUid());

            if(userDTO == null){
                throw new IncorrectUserException();
            }

            if(!userDTO.getPassword().equals(request.getPassword())){
                throw new IncorrectPasswordException();
            }

            log.info(ErrorCode.NO_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .errorCode(ErrorCode.NO_ERROR.ordinal())
                    .message(ErrorCode.NO_ERROR.getMessage())
                    .build();

        }
        catch (IncorrectUserException e){

            log.info(ErrorCode.INCORRECT_USER.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.INCORRECT_USER.ordinal())
                    .message(ErrorCode.INCORRECT_USER.getMessage())
                    .build();

        }
        catch (IncorrectPasswordException e){

            log.info(ErrorCode.INCORRECT_PASSWORD.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.INCORRECT_PASSWORD.ordinal())
                    .message(ErrorCode.INCORRECT_PASSWORD.getMessage())
                    .build();

        }
        catch (Exception e){

            log.info(ErrorCode.UNEXPECTED_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.UNEXPECTED_ERROR.ordinal())
                    .message(ErrorCode.UNEXPECTED_ERROR.getMessage())
                    .build();

        }
    }


    @PutMapping("/updatePassword")
    public ResponseDTO updatePassword(@RequestBody UserDTO request) {

        try{

            log.info(divideLog);
            log.info("/api/updatePassword {}", startMessage);

            UserDTO userDTO = userService.getUserInfoById(request.getUid());

            if(userDTO == null){
                throw new IncorrectUserException();
            }

            userDTO.setPassword(request.getPassword());
            userDTO.setModDate(LocalDateTime.now());

            userService.register(userDTO);

            return ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .errorCode(ErrorCode.NO_ERROR.ordinal())
                    .message(ErrorCode.NO_ERROR.getMessage())
                    .build();

        }
        catch (IncorrectUserException e){

            log.info(ErrorCode.INCORRECT_USER.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.INCORRECT_USER.ordinal())
                    .message(ErrorCode.INCORRECT_USER.getMessage())
                    .build();

        }
        catch (Exception e){

            log.info(ErrorCode.UNEXPECTED_ERROR.getMessage());

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.UNEXPECTED_ERROR.ordinal())
                    .message(ErrorCode.UNEXPECTED_ERROR.getMessage())
                    .build();
        }
        finally {

            log.info("uid : {}", request.getUid());
            log.info("/api/updatePassword {}", endMessage);
            log.info(divideLog);

        }
    }


    @GetMapping("/isValidId")
    public ResponseDTO isValidId(@RequestParam String uid){

        try{
            if(userService.getUserInfoById(uid) != null){
                throw new AlreadyIdExistException();
            }

            return ResponseDTO.builder()
                    .httpCode(200)
                    .error(false)
                    .errorCode(ErrorCode.NO_ERROR.ordinal())
                    .message(ErrorCode.NO_ERROR.getMessage())
                    .content(UserDTO.builder().uid(uid).build())
                    .build();

        }
        catch (AlreadyIdExistException e){

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.ALREADY_ID_EXIST.ordinal())
                    .message(ErrorCode.ALREADY_ID_EXIST.getMessage())
                    .content(UserDTO.builder().uid(uid).build())
                    .build();

        }
        catch (Exception e){

            log.info(ErrorCode.UNEXPECTED_ERROR.getMessage());
            log.info("/isValidId");

            return ResponseDTO.builder()
                    .httpCode(400)
                    .error(true)
                    .errorCode(ErrorCode.UNEXPECTED_ERROR.ordinal())
                    .message(ErrorCode.UNEXPECTED_ERROR.getMessage())
                    .build();

        }
    }
}

