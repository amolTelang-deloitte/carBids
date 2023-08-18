package com.CarBids.carBidsbiddingservice.Event.EventManager;

import com.CarBids.carBidsbiddingservice.Event.BidPlacedEvent;
import com.CarBids.carBidsbiddingservice.entity.Bid;
import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import com.CarBids.carBidsbiddingservice.repository.BidCollectionRepository;
import com.CarBids.carBidsbiddingservice.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Component
public class BidQueueManager implements ApplicationListener<BidPlacedEvent> {

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
    public void onApplicationEvent(BidPlacedEvent event){

        Bid newBid = new Bid();
        newBid.setBidValue(event.getBid());
        newBid.setCollectionId(event.getCollectionId());
        newBid.setUserId(event.getUserId());

        BidCollection lotCollection = bidCollectionRepository.findOneBylotId(event.getCollectionId());

        if(lotCollection.getCurrentHighestBid() == null && lotCollection.getHighestBidUserId() == null){
            if(Integer.parseInt(newBid.getBidValue()) <= Integer.parseInt(lotCollection.getStartingValue())){
                    //throw exception here
                System.out.println("Invald 1 ");
                return;
            }
            enqueueBid(newBid);
            lotCollection.setCurrentHighestBid(newBid.getBidValue());
            lotCollection.setHighestBidUserId(newBid.getUserId());
            bidRepository.save(newBid);
            bidCollectionRepository.save(lotCollection);
        }else{
            if(bidQueue.isEmpty()){
                if(Integer.parseInt(newBid.getBidValue()) < Integer.parseInt(lotCollection.getCurrentHighestBid())){
                    //throw exception here
                    System.out.println("Invald 2 ");
                    return;
                }
                enqueueBid(newBid);
                lotCollection.setCurrentHighestBid(newBid.getBidValue());
                lotCollection.setHighestBidUserId(newBid.getUserId());
                bidRepository.save(newBid);
                bidCollectionRepository.save(lotCollection);
            }
            else{
                System.out.println(bidQueue.poll().getBidValue());
                if (Integer.parseInt(newBid.getBidValue()) > Integer.parseInt(bidRepository.findBycollectionIdOrderByBidValueDesc(newBid.getCollectionId()).get(0).getBidValue())) {
                    bidQueue.add(newBid);
                    lotCollection.setCurrentHighestBid(newBid.getBidValue());
                    lotCollection.setHighestBidUserId(newBid.getUserId());
                    bidRepository.save(newBid);
                    bidCollectionRepository.save(lotCollection);
                } else {
                    //thorow exception here
                    System.out.println("New bid's value is not greater than the last bid's value.");
                    return;
                }
            }
        }
    }
}
