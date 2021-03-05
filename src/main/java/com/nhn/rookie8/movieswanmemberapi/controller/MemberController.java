package com.nhn.rookie8.movieswanmemberapi.controller;


import com.nhn.rookie8.movieswanmemberapi.dto.*;
import com.nhn.rookie8.movieswanmemberapi.service.MemberService;
import com.nhn.rookie8.movieswanmemberapi.memberenum.ErrorCode;
import com.nhn.rookie8.movieswanmemberapi.memberexception.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/register")
    public ResponseDTO register(@RequestBody MemberRegisterDTO request) {

        if(!memberService.check(request)){
            throw new InputErrorException();
        }

        if(memberService.alreadyMemberExist(request)){
            throw new IdOrPasswordErrorException();
        }

        memberService.register(request);

        return memberService.responseWithoutContent(ErrorCode.NO_ERROR);
    }


    @PostMapping("/auth")
    public ResponseDTO auth(@RequestBody MemberAuthDTO request) {

        if(!memberService.check(request)){
            throw new InputErrorException();
        }

        MemberIdNameDTO memberIdNameDTO = memberService.authenticate(request);

        if(memberIdNameDTO == null){
            throw new IdOrPasswordErrorException();
        }

        return memberService.responseWithContent(ErrorCode.NO_ERROR, memberIdNameDTO);
    }


    @PostMapping("/getMemberInfo")
    public ResponseDTO getMemberInfo(@RequestBody MemberIdDTO request) {

        if(!memberService.check(request)){
            throw new InputErrorException();
        }

        MemberDTO memberDTO = memberService.getMemberInfoById(request.getMemberId());

        if(memberDTO == null){
            throw new IdOrPasswordErrorException();
        }

        return memberService.responseWithContent(ErrorCode.NO_ERROR, memberDTO);
    }



    @GetMapping("/isExistId")
    public ResponseDTO isExistId(@RequestParam String memberId) {

        if(!memberService.checkString(memberId)){
            throw new InputErrorException();
        }

        if(memberService.getMemberInfoById(memberId) != null){
            throw new IdOrPasswordErrorException();
        }

        return memberService.responseWithoutContent(ErrorCode.NO_ERROR);
    }

}

