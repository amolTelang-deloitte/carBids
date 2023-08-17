package com.CarBids.carBidsauthenticationservice.exception.exceptions;

public class UserAlreadyExistsException extends  RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
