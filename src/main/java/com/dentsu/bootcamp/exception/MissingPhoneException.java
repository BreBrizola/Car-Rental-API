package com.dentsu.bootcamp.exception;

public class MissingPhoneException extends RuntimeException {
    public MissingPhoneException(String message){
        super(message);
    }
}
