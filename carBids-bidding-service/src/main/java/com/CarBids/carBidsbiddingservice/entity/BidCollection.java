package com.CarBids.carBidsbiddingservice.entity;

import com.CarBids.carBidsbiddingservice.Event.EventManager.BidQueueManager;
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
    private Long lotid;
    private String currentHighestBid;
    @NonNull
    private String biddingStatus;
    @NonNull
    private String startingValue;
    @NonNull
    private LocalDateTime startTimestamp;
    @NonNull
    private LocalDateTime endTimeStamp;

    @Autowired
    private transient BidQueueManager bidQueueManager;

    public void placeBid(String bidValue,Long lotId,Long userId){
        bidQueueManager.enqueueBid(lotId,bidValue,userId);
    }

}
