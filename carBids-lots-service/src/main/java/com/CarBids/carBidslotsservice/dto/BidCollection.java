package com.CarBids.carBidslotsservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidCollection {
    private String currentHighestBid;
    private Long highestBidUserId;
    private String highestBidUsername;
    private Integer noOfBids;

}
