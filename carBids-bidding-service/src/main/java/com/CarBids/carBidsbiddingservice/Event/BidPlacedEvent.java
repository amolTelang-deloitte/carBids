package com.CarBids.carBidsbiddingservice.Event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class BidPlacedEvent extends ApplicationEvent {
    private final Long collectionId;
    private final Long userId;
    private final String bidValue;

    public BidPlacedEvent( Object source,Long collectionId, Long userId, String bidValue) {
        super(source);
        this.collectionId = collectionId;
        this.userId = userId;
        this.bidValue = bidValue;
    }
    public Long getUserId() {
        return userId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public String getBid() {
        return bidValue;
    }
}
