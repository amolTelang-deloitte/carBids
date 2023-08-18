package com.CarBids.carBidsbiddingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidDetails {
    private Long lotId;
    private String bidValue;
}

