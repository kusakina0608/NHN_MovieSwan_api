package com.nhn.rookie8.movieswanuserapi.userException;

public class IncorrectUserException extends Exception{
    public IncorrectUserException(){
        super();
    }
    public IncorrectUserException(String message){
        super(message);
    }
}
