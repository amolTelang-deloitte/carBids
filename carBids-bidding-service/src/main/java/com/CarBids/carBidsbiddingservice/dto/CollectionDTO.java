package com.CarBids.carBidsbiddingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionDTO {
    private String currentHighestBid;
    private Long highestBidUserId;
    private String highestBidUsername;
    private Integer noOfBids;

}
