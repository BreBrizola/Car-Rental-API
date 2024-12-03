package com.dentsu.bootcamp.exception;

public class ReservationNotFoundException extends NullPointerException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
