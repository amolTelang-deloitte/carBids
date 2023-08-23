package com.CarBids.CarBidscommentservice.exception;

import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidContentException;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidCredentialsException;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidIdException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.swing.*;
import java.net.ConnectException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    //invalid credentails exception
    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentailsException(final InvalidCredentialsException ex, WebRequest request){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionDetails errorDetails = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorDetails,status);
    }

    //invalid id exception
    @ExceptionHandler(value = InvalidIdException.class)
    public ResponseEntity<?> handleInvalidReplyException(final InvalidIdException ex, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails errorDetails = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorDetails,status);
    }

    //invalid content exception
    @ExceptionHandler(value = InvalidContentException.class)
    public ResponseEntity<?> handleInvalidContentException(final InvalidContentException ex, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDetails errorDetails = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorDetails,status);

    }

    @ExceptionHandler(value = ConnectException.class)
    public ResponseEntity<?> handleConnectionException(final ConnectException exception, WebRequest request){
        HttpStatus unauth = HttpStatus.SERVICE_UNAVAILABLE;
        ExceptionDetails exceptionDetails = new ExceptionDetails("One or more service is down, Try again later",unauth,ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(exceptionDetails,HttpStatus.SERVICE_UNAVAILABLE);

    }
    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJWT(final ExpiredJwtException exception, WebRequest request){
        HttpStatus unauth = HttpStatus.UNAUTHORIZED;
        ExceptionDetails exceptionDetails = new ExceptionDetails(exception.getMessage(), unauth,ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(exceptionDetails,HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleGenericException(final Exception exception, WebRequest request){
        HttpStatus unauth = HttpStatus.SERVICE_UNAVAILABLE;
        ExceptionDetails errorDetails = new ExceptionDetails("One or more services are down, Try again later", unauth, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);

    }



}
