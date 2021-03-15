package com.nhn.rookie8.movieswanmemberapi;

import com.nhn.rookie8.movieswanmemberapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import com.nhn.rookie8.movieswanmemberapi.memberexception.BadRequestException;
import com.nhn.rookie8.movieswanmemberapi.memberexception.IdOrPasswordErrorException;
import com.nhn.rookie8.movieswanmemberapi.memberexception.MemberException;
import com.nhn.rookie8.movieswanmemberapi.memberexception.UnauthorizedException;
import com.nhn.rookie8.movieswanmemberapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@Log4j2
@ControllerAdvice
@ResponseBody
public class MemberControllerAdvice {

    private final MemberService memberService;

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public void badRequestException(BadRequestException e) { }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public void unauthorizedException(UnauthorizedException e) { }

    @ExceptionHandler(MemberException.class)
    public ResponseDTO exceptionControl(MemberException e) {
        if(e instanceof IdOrPasswordErrorException){
            return memberService.responseWithoutContent(ErrorCode.ID_OR_PASSWORD_ERROR);
        }

        return memberService.responseWithoutContent(ErrorCode.INPUT_ERROR);
    }
}
