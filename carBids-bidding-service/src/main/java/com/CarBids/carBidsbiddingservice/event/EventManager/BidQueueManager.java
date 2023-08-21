package com.CarBids.carBidsbiddingservice.event.EventManager;

import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import com.CarBids.carBidsbiddingservice.event.BidPlacedEvent;
import com.CarBids.carBidsbiddingservice.entity.Bid;
import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import com.CarBids.carBidsbiddingservice.exception.exceptions.ClosedAutionException;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidDataException;
import com.CarBids.carBidsbiddingservice.repository.BidCollectionRepository;
import com.CarBids.carBidsbiddingservice.repository.BidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class BidQueueManager implements ApplicationListener<BidPlacedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(BidPlacedEvent.class);
    private final BidRepository bidRepository;
    private final BidCollectionRepository bidCollectionRepository;

    @Autowired
    public BidQueueManager(BidRepository bidRepository, BidCollectionRepository bidCollectionRepository){
        this.bidCollectionRepository = bidCollectionRepository;
        this.bidRepository = bidRepository;
    }
    private final Queue<Bid> bidQueue = new LinkedList<>();

    public void enqueueBid(Bid bid){
        bidQueue.add(bid);
    }

    @Override
    public void onApplicationEvent(BidPlacedEvent event) {
        try {
            logger.info("Received Bid Placement Event"+" "+ LocalDateTime.now());

            Bid newBid = new Bid();
            newBid.setBidValue(event.getBid());
            newBid.setCollectionId(event.getCollectionId());
            newBid.setUserId(event.getUserId());
            newBid.setUserName(event.getUsername());

            BidCollection lotCollection = bidCollectionRepository.findOneBylotId(event.getCollectionId());
            if (lotCollection.getBiddingStatus().equals(CollectionStatus.CLOSED)){
                logger.error("Attempting to place bid on closed Lot"+" "+LocalDateTime.now());
                throw new ClosedAutionException("Auction is Closed, No bidding possible");
            }


            if (lotCollection.getCurrentHighestBid() == null && lotCollection.getHighestBidUserId() == null) {
                if (Integer.parseInt(newBid.getBidValue()) <= Integer.parseInt(lotCollection.getStartingValue())) {
                    logger.warn("Invalid amount entered"+" "+newBid.getBidValue()+" "+ LocalDateTime.now());
                    throw new InvalidDataException("Please bid higher amount");
                }
                enqueueBid(newBid);
                lotCollection.setCurrentHighestBid(newBid.getBidValue());
                lotCollection.setHighestBidUserId(newBid.getUserId());
                lotCollection.setHighestBidUsername(newBid.getUserName());
                if(lotCollection.getNoOfBids() == null)
                    lotCollection.setNoOfBids(0);
                lotCollection.setNoOfBids(lotCollection.getNoOfBids() + 1);
                bidRepository.save(newBid);
                logger.info("Successfully saved Bid"+" "+LocalDateTime.now());
                bidCollectionRepository.save(lotCollection);
            } else {
                if (bidQueue.isEmpty()) {
                    if (Integer.parseInt(newBid.getBidValue()) < Integer.parseInt(lotCollection.getCurrentHighestBid())) {
                        logger.warn("Invalid amount entered"+" "+newBid.getBidValue()+" "+ LocalDateTime.now());
                        throw new InvalidDataException("Please bid higher amount");
                    }
                    enqueueBid(newBid);
                    lotCollection.setCurrentHighestBid(newBid.getBidValue());
                    lotCollection.setHighestBidUserId(newBid.getUserId());
                    lotCollection.setHighestBidUsername(newBid.getUserName());
                    if(lotCollection.getNoOfBids() == null)
                        lotCollection.setNoOfBids(0);
                    lotCollection.setNoOfBids(lotCollection.getNoOfBids() + 1);
                    bidRepository.save(newBid);
                    logger.info("Successfully saved Bid"+" "+LocalDateTime.now());
                    bidCollectionRepository.save(lotCollection);
                } else {
                    if (Integer.parseInt(newBid.getBidValue()) > Integer.parseInt(bidRepository.findBycollectionIdOrderByBidValueDesc(newBid.getCollectionId()).get(0).getBidValue())) {
                        bidQueue.add(newBid);
                        lotCollection.setCurrentHighestBid(newBid.getBidValue());
                        lotCollection.setHighestBidUserId(newBid.getUserId());
                        lotCollection.setHighestBidUsername(newBid.getUserName());
                        if(lotCollection.getNoOfBids() == null)
                            lotCollection.setNoOfBids(0);
                        lotCollection.setNoOfBids(lotCollection.getNoOfBids() + 1);
                        bidRepository.save(newBid);
                        logger.info("Successfully saved Bid"+" "+LocalDateTime.now());
                        bidCollectionRepository.save(lotCollection);
                    } else {
                        logger.warn("Invalid amount entered"+" "+newBid.getBidValue()+" "+ LocalDateTime.now());
                        throw new InvalidDataException("Please bid higher amount");
                    }
                }
            }
        }catch(Exception ex){
            throw new InvalidDataException(ex.getMessage());
        }
    }
}
