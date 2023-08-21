package com.CarBids.carBidsbiddingservice.exception;

import com.CarBids.carBidsbiddingservice.exception.exceptions.ClosedAutionException;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidIdException;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidDataException;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidUserException;
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

    //closed auction exception
    @ExceptionHandler(value = ClosedAutionException.class)
    public ResponseEntity<Object> handleClosedAuctionException(ClosedAutionException ex){
        HttpStatus status = HttpStatus.FORBIDDEN;
        ExceptionDetails e = new ExceptionDetails(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(e,status);
    }


    //invalid user exception
    @ExceptionHandler(value = InvalidUserException.class)
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
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

    //generic exception
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleGenericException(final Exception exception, WebRequest request){
        HttpStatus unauth = HttpStatus.UNAUTHORIZED;
        ExceptionDetails errorDetails = new ExceptionDetails(exception.getMessage(), unauth, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

}
