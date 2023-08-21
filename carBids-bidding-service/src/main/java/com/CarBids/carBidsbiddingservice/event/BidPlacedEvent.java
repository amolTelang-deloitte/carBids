package com.CarBids.carBidsbiddingservice.event;

import org.springframework.context.ApplicationEvent;

public class BidPlacedEvent extends ApplicationEvent {
    private final Long collectionId;
    private final Long userId;

    private final String username;
    private final String bidValue;

    public BidPlacedEvent(Object source, Long collectionId, Long userId, String username, String bidValue) {
        super(source);
        this.collectionId = collectionId;
        this.userId = userId;
        this.username = username;
        this.bidValue = bidValue;
    }
    public Long getUserId() {
        return userId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public String getUsername(){return username;}

    public String getBid() {
        return bidValue;
    }
}
