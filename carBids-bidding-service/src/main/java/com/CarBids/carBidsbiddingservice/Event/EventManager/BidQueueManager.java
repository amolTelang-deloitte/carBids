package com.CarBids.carBidsbiddingservice.Event.EventManager;

import com.CarBids.carBidsbiddingservice.Event.BidPlacedEvent;
import com.CarBids.carBidsbiddingservice.entity.Bid;
import com.CarBids.carBidsbiddingservice.repository.BidCollectionRepository;
import com.CarBids.carBidsbiddingservice.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.*;

public class BidQueueManager implements ApplicationListener<BidPlacedEvent> {
    private final Map<Long, Queue<String>> bidQueue = new HashMap<>();

    @Autowired
    private BidRepository bidRepository;
    private BidCollectionRepository bidCollectionRepository;

    public void enqueueBid(Long collectionId,String bidValue,Long userId){
        bidQueue.computeIfAbsent(collectionId, k -> new LinkedList<>()).offer(bidValue);
    }

    public String dequeueBid(Long collectionId){
        Queue<String> queue = bidQueue.getOrDefault(collectionId, new LinkedList<>());
        return queue.poll();
    }

    @Override
    public void onApplicationEvent(BidPlacedEvent event){
        String newBidValue = event.getBid();
        Long collectionId = event.getCollectionId();
        Long userId = event.getUserId();

        List<Bid> existingBids = bidRepository.findBycollectionIdOrderByBidValueDesc(collectionId);
        if(!existingBids.isEmpty()){
            String highestBid = existingBids.get(0).getBidValue();
            if(newBidValue.compareTo(highestBid)<=0){
                System.out.println("wrong bid value XXXXXXXXXXXXXX");
                return;
            }
        }

        enqueueBid(collectionId,newBidValue,userId);

        Bid newBid = new Bid();
        newBid.setBidValue(newBidValue);
        newBid.setCollectionId(collectionId);
        newBid.setUserId(userId);
        bidRepository.save(newBid);
    }
}
