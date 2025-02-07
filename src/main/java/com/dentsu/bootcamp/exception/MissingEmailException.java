package com.dentsu.bootcamp.exception;

public class MissingEmailException extends RuntimeException {
    public MissingEmailException(String message) {
        super(message);
    }
}

