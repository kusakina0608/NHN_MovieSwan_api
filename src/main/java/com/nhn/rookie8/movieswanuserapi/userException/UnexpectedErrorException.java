package com.nhn.rookie8.movieswanuserapi.userException;

public class UnexpectedErrorException extends Exception{
    public UnexpectedErrorException(){
        super();
    }
    public UnexpectedErrorException(String message){
        super(message);
    }
}
