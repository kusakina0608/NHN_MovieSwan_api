package com.nhn.rookie8.movieswanmemberapi;

import com.nhn.rookie8.movieswanmemberapi.dto.ResponseDTO;
import com.nhn.rookie8.movieswanmemberapi.service.MemberService;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import com.nhn.rookie8.movieswanmemberapi.memberexception.IdOrPasswordErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Log4j2
@ControllerAdvice
@ResponseBody
public class MemberControllerAdvice {

    private final MemberService memberService;

    @ExceptionHandler(Exception.class)
    public ResponseDTO exceptionControl(Exception e) {
        if(e instanceof IdOrPasswordErrorException){
            return memberService.responseWithoutContent(ErrorCode.ID_OR_PASSWORD_ERROR);
        }
        return memberService.responseWithoutContent(ErrorCode.INPUT_ERROR);
    }
}
