package com.nhn.rookie8.movieswanuserapi.userenum;

import lombok.Getter;

@Getter
public enum ErrorCode{
    NO_ERROR("Request success."),
    ALREADY_ID_EXIST("Id already exist."),
    INCORRECT_USER("No user matched."),
    INCORRECT_PASSWORD("Incorrect password."),
    UNEXPECTED_ERROR("Unexpected error");

    private String message;

    ErrorCode(String message){
        this.message = message;
    }
}