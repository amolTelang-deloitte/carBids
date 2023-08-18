package com.CarBids.carBidsbiddingservice.service;

import com.CarBids.carBidsbiddingservice.Event.BidPlacedEvent;
import com.CarBids.carBidsbiddingservice.dto.BidDetails;
import com.CarBids.carBidsbiddingservice.dto.CollectionDetails;
import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import com.CarBids.carBidsbiddingservice.repository.BidCollectionRepository;
import com.CarBids.carBidsbiddingservice.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BidService implements IBidService{

    private final ApplicationEventPublisher eventPublisher;
    private final BidCollectionRepository bidCollectionRepository;
    private final BidRepository bidRepository;


    @Autowired
    public BidService(ApplicationEventPublisher eventPublisher, BidCollectionRepository bidCollectionRepository, BidRepository bidRepository) {
        this.eventPublisher = eventPublisher;
        this.bidCollectionRepository = bidCollectionRepository;
        this.bidRepository = bidRepository;
    }

    @Override
    public ResponseEntity<?> saveBidCollection(CollectionDetails bidCollection) {
        BidCollection newBidCollection = BidCollection.builder()
                .lotId(bidCollection.getLotId())
                .currentHighestBid(null)
                .biddingStatus(CollectionStatus.RUNNING)
                .startingValue(bidCollection.getStartingValue())
                .startTimestamp(LocalDateTime.now())
                .endTimeStamp(null)
                .build();

         bidCollectionRepository.save(newBidCollection);
         return new ResponseEntity<>(newBidCollection, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> addBid(BidDetails bidDetails, Long userId) {
        eventPublisher.publishEvent(new BidPlacedEvent(this,bidDetails.getLotId(),userId,bidDetails.getBidValue()));
        return new ResponseEntity<>("createdBid",HttpStatus.CREATED);
    }
}
