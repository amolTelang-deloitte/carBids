package com.CarBids.carBidsbiddingservice.entity;

import com.CarBids.carBidsbiddingservice.Event.EventManager.BidQueueManager;
import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    @NonNull
    private CollectionStatus biddingStatus;
    @NonNull
    private String startingValue;
    @NonNull
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimeStamp;

}
