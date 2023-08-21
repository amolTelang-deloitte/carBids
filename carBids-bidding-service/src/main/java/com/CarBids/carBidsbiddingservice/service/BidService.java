package com.CarBids.carBidsbiddingservice.service;

import com.CarBids.carBidsbiddingservice.dto.*;
import com.CarBids.carBidsbiddingservice.event.BidPlacedEvent;
import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidDataException;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidIdException;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidUserException;
import com.CarBids.carBidsbiddingservice.feignClient.AuthFeignClient;
import com.CarBids.carBidsbiddingservice.repository.BidCollectionRepository;
import com.CarBids.carBidsbiddingservice.repository.BidRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
    private final AuthFeignClient authFeignClient;
    private final BidRepository bidRepository;


    @Autowired
    public BidService(ApplicationEventPublisher eventPublisher, BidCollectionRepository bidCollectionRepository, AuthFeignClient authFeignClient, BidRepository bidRepository) {
        this.eventPublisher = eventPublisher;
        this.bidCollectionRepository = bidCollectionRepository;
        this.authFeignClient = authFeignClient;
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
         return new ResponseEntity<>("",HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> addBid(BidDetails bidDetails, Long userId) {
        if(!checkUserId(userId))
            throw new InvalidUserException("Invalid User Id, Check again");
        if(bidCollectionRepository.findOneBylotId(bidDetails.getLotId()) == null)
            throw new InvalidIdException("Invalid Lot Id, Check again");
        String username = getUsername(userId);
        eventPublisher.publishEvent(new BidPlacedEvent(this,bidDetails.getLotId(),userId, username, bidDetails.getBidValue()));
        return new ResponseEntity<>("createdBid",HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> closeBidding(Long lotId) {
        BidCollection collection = bidCollectionRepository.findOneBylotId(lotId);
                collection.setBiddingStatus(CollectionStatus.CLOSED);
                bidCollectionRepository.save(collection);
        return new ResponseEntity<>("successfully closed bidding no longer available",HttpStatus.OK);
    }

    @Override
    public CollectionDTO getCollection(Long lotId) {
        BidCollection bidCollection = bidCollectionRepository.findOneBylotId(lotId);
        if(bidCollection == null)
            throw new InvalidIdException("Invalid lotId, Check again");
        CollectionDTO collectionDTO = CollectionDTO.builder()
                .currentHighestBid(bidCollection.getCurrentHighestBid())
                .highestBidUsername(bidCollection.getHighestBidUsername())
                .highestBidUserId(bidCollection.getHighestBidUserId())
                .noOfBids(bidCollection.getNoOfBids())
                .build();
        return collectionDTO;
    }

    @HystrixCommand(groupKey = "auth", commandKey = "username",fallbackMethod = "authServiceFallback")
    public String getUsername(Long userId){
        String username = authFeignClient.getUsername(userId);
        if(username.isEmpty())
        {
            throw new InvalidDataException("Invalid UserId");
        }
        return username;
    }

    @HystrixCommand(groupKey = "auth", commandKey = "userId",fallbackMethod = "authServiceFallback")
    public Boolean checkUserId(Long userId){

        ResponseEntity<UserIdCheck> response = authFeignClient.checkUserId(userId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().getCheck();
        } else {
            throw new InvalidDataException("Invalid UserId");
        }

    }

    public ResponseEntity<?> authServiceFallback(){
        FallbackResponse fallbackResponse = FallbackResponse.builder()
                .message("Authentication Service is down, Try again later.")
                .timeStamp(LocalDateTime.now())
                .httpCode(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
        return new ResponseEntity<>(fallbackResponse,HttpStatus.SERVICE_UNAVAILABLE);
    }
}
