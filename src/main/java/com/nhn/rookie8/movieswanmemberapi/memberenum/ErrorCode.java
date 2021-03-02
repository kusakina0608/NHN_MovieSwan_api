package com.nhn.rookie8.movieswanmemberapi.memberenum;

import lombok.Getter;

@Getter
public enum ErrorCode{
    NO_ERROR("Request success."),
    ID_OR_PASSWORD_ERROR("Id or password error"),
    INPUT_ERROR("Input error");

    private String message;

    ErrorCode(String message){
        this.message = message;
    }
}