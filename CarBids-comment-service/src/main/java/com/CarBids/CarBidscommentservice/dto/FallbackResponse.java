package com.CarBids.CarBidscommentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FallbackResponse {
    private String message;
    private LocalDateTime timeStamp;
    private HttpStatus httpCode;
}
