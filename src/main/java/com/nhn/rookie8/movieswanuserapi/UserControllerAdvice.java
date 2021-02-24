package com.nhn.rookie8.movieswanuserapi;

import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import com.nhn.rookie8.movieswanuserapi.userexception.AlreadyIdExistException;
import com.nhn.rookie8.movieswanuserapi.userexception.IncorrectPasswordException;
import com.nhn.rookie8.movieswanuserapi.userexception.IncorrectUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Log4j2
@ControllerAdvice
@ResponseBody
public class UserControllerAdvice {

    private final UserService userService;

    @ExceptionHandler(Exception.class)
    public ResponseDTO exceptionControl(Exception e) {
        if(e.getClass()== AlreadyIdExistException.class){
            return userService.returnResponseDto(ErrorCode.ALREADY_ID_EXIST, null);
        }
        else if(e.getClass()== IncorrectPasswordException.class){
            return userService.returnResponseDto(ErrorCode.INCORRECT_PASSWORD, null);
        }
        else if(e.getClass()== IncorrectUserException.class){
            return userService.returnResponseDto(ErrorCode.INCORRECT_USER, null);
        }
        return userService.returnResponseDto(ErrorCode.UNEXPECTED_ERROR, null);
    }
}
