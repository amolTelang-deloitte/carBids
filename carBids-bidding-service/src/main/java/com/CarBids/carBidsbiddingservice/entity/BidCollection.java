package com.CarBids.carBidsbiddingservice.entity;

import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="bids_collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collectionId;
    @NonNull
    private Long lotId;
    private String currentHighestBid;
    private Long highestBidUserId;
    private String highestBidUsername;
    @Column(columnDefinition = "integer default 0")
    private Integer noOfBids;
    @NonNull
    private CollectionStatus biddingStatus;
    @NonNull
    private String startingValue;
    @NonNull
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimeStamp;

}
