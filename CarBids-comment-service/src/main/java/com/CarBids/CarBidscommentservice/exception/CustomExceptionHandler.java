package com.CarBids.CarBidscommentservice.exception;

import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidContentException;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidCredentialsException;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidIdException;
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

    //generic exception
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleGenericException(final Exception exception, WebRequest request){
        HttpStatus unauth = HttpStatus.UNAUTHORIZED;
        ExceptionDetails errorDetails = new ExceptionDetails(exception.getMessage(), unauth, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

}
