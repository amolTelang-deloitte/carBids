package com.CarBids.carBidsbiddingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="bids_collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collectionId;
    @NonNull
    private Long lotid;
    @NonNull
    private String currentHighestBid;
    @NonNull
    private String biddingStatus;
    @NonNull
    private LocalDateTime startTimestamp;
    @NonNull
    private LocalDateTime endTimeStamp;

}
