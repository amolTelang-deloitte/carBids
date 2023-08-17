package com.CarBids.carBidsauthenticationservice.exception;

import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidPasswordException;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.NotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    //invalid base64 string
    @ExceptionHandler(value = InvalidBase64Exception.class)
    public ResponseEntity<Object> handleInvalidBase64Exception(InvalidBase64Exception exception){
        HttpStatus unauth = HttpStatus.UNAUTHORIZED;
        ExceptionDetails e = new ExceptionDetails(exception.getMessage(), unauth, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e, unauth);
    }

    //generic exception
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleGenericException(final Exception exception, WebRequest request){
        HttpStatus unauth = HttpStatus.UNAUTHORIZED;
        ExceptionDetails errorDetails = new ExceptionDetails(exception.getMessage(), unauth, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

}
