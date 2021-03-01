package com.nhn.rookie8.movieswanuserapi;

import com.nhn.rookie8.movieswanuserapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanuserapi.service.UserService;
import com.nhn.rookie8.movieswanuserapi.userenum.ErrorCode;
import com.nhn.rookie8.movieswanuserapi.userexception.IdOrPasswordErrorException;
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
        if(e instanceof IdOrPasswordErrorException){
            return userService.responseWithoutContent(ErrorCode.ID_OR_PASSWORD_ERROR);
        }
        return userService.responseWithoutContent(ErrorCode.INPUT_ERROR);
    }
}
