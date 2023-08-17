package com.CarBids.carBidsauthenticationservice.exception;


public class InvalidBase64Exception extends RuntimeException {
    public InvalidBase64Exception(String message) {
        super(message);
    }


    public InvalidBase64Exception(String message, Throwable cause) {
        super(message, cause);
    }
}