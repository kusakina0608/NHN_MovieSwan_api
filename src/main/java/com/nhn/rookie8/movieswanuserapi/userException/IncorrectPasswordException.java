package com.nhn.rookie8.movieswanuserapi.userException;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException(){
        super();
    }
    public IncorrectPasswordException(String message){
        super(message);
    }
}
