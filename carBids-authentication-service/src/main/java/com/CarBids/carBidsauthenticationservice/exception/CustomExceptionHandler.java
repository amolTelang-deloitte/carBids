package com.CarBids.carBidsauthenticationservice.exception;

import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidBase64Exception;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidCredentialsException;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidIdException;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.UserAlreadyExistsException;
import org.omg.CORBA.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    //invalid userId
    @ExceptionHandler(value = InvalidIdException.class)
    public ResponseEntity<Object> handleInvalidIdException(InvalidIdException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);

    }

    //user already exists exception
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception){
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionDetails e = new ExceptionDetails(exception.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);

    }

    //invalid base64 string
    @ExceptionHandler(value = InvalidBase64Exception.class)
    public ResponseEntity<Object> handleInvalidBase64Exception(InvalidBase64Exception exception){
        HttpStatus unauth = HttpStatus.BAD_REQUEST;
        ExceptionDetails e = new ExceptionDetails(exception.getMessage(), unauth, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e, unauth);
    }

    //invalid credentials exception
    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException exception){
        HttpStatus unauth = HttpStatus.BAD_REQUEST;
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
