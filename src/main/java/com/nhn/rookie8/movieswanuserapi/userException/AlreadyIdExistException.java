package com.nhn.rookie8.movieswanuserapi.userException;

public class AlreadyIdExistException extends Exception{
    public AlreadyIdExistException(){
        super();
    }
    public AlreadyIdExistException(String message){
        super(message);
    }
}
