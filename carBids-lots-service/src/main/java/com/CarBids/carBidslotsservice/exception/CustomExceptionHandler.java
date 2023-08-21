package com.CarBids.carBidslotsservice.exception;

import com.CarBids.carBidslotsservice.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    //invalid user exception
    @ExceptionHandler(value = InvalidUserException.class)
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException ex){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }

    //invalid Id exception
    @ExceptionHandler(value = InvalidIdException.class)
    public ResponseEntity<Object> handleInvalidException(InvalidIdException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }

    //invalid Data exception
    @ExceptionHandler(value = InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }

    //invalid type exception
    @ExceptionHandler( value = InvalidTypeException.class)
    public ResponseEntity<Object> handleInvalidException(InvalidTypeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }

    //invalid auth token exception
    @ExceptionHandler(value = InvalidAuthException.class)
    public ResponseEntity<Object> handleInvalidAuthException(InvalidAuthException ex){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }


    //generic exception
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleGenericException(final Exception exception, WebRequest request){
        HttpStatus unauth = HttpStatus.UNAUTHORIZED;
        ExceptionDetails errorDetails = new ExceptionDetails(exception.getMessage(), unauth, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

}
