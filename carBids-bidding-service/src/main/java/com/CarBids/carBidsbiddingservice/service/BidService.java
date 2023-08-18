package com.CarBids.carBidsbiddingservice.service;

import com.CarBids.carBidsbiddingservice.dto.CollectionDetails;
import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import org.springframework.http.ResponseEntity;

public class BidService implements IBidService{
    @Override
    public ResponseEntity<?> saveBidCollection(CollectionDetails bidCollection) {
        return null;
    }

    @Override
    public ResponseEntity<?> addBid(Long lotId, String bidValue, Long userId) {
        return null;
    }
}
