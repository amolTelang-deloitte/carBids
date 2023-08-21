package com.CarBids.carBidsbiddingservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ExceptionDetails {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timeStamp;

}
