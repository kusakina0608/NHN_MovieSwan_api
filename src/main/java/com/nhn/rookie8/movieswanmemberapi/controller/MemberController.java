package com.nhn.rookie8.movieswanmemberapi.controller;


import com.nhn.rookie8.movieswanmemberapi.dto.*;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import com.nhn.rookie8.movieswanmemberapi.memberexception.BadRequestException;
import com.nhn.rookie8.movieswanmemberapi.memberexception.IdOrPasswordErrorException;
import com.nhn.rookie8.movieswanmemberapi.memberexception.InputErrorException;
import com.nhn.rookie8.movieswanmemberapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/register")
    public ResponseDTO register(@RequestBody MemberRegisterDTO request) {

        if(!memberService.checkInput(request)){
            throw new InputErrorException();
        }

        if(memberService.alreadyMemberExist(request.getMemberId())){
            throw new IdOrPasswordErrorException();
        }

        memberService.register(request);

        return memberService.responseWithoutContent(ErrorCode.NO_ERROR);
    }


    @PostMapping("/auth")
    public TokenDTO auth(@RequestBody MemberAuthDTO request) {

        if(!memberService.checkInput(request))
            throw new BadRequestException();

        MemberIdNameDTO memberIdNameDTO = memberService.externalAuthenticate(request);

        return memberService.responseWithToken(memberService.getToken(memberIdNameDTO.getMemberId()));
    }

    @GetMapping("/verifyToken")
    public ResponseDTO verifyToken(HttpServletRequest request) {
        return memberService.responseWithContent(ErrorCode.NO_ERROR,
                memberService.getMemberIdNameDTO((String) request.getAttribute("memberId")));
    }

    @PostMapping("/getMemberInfo")
    public ResponseDTO getMemberInfo(@RequestBody MemberIdDTO request) {

        if(!memberService.checkInput(request)){
            throw new InputErrorException();
        }

        MemberDTO memberDTO = memberService.getMemberInfoById(request.getMemberId());

        return memberService.responseWithContent(ErrorCode.NO_ERROR, memberDTO);
    }



    @GetMapping("/isExistId")
    public ResponseDTO isExistId(@RequestParam String memberId) {

        if(!memberService.checkString(memberId)){
            throw new InputErrorException();
        }

        if(memberService.alreadyMemberExist(memberId)){
            throw new IdOrPasswordErrorException();
        }

        return memberService.responseWithoutContent(ErrorCode.NO_ERROR);
    }

}

