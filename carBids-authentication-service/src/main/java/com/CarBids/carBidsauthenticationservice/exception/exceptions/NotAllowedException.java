package com.CarBids.carBidsauthenticationservice.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotAllowedException(String message){
        super(message);
    }
}
