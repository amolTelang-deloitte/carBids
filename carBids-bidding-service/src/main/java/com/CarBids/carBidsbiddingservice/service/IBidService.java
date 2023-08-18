package com.CarBids.carBidsbiddingservice.service;

import com.CarBids.carBidsbiddingservice.dto.BidDetails;
import com.CarBids.carBidsbiddingservice.dto.CollectionDetails;
import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import org.springframework.http.ResponseEntity;

public interface IBidService {
    ResponseEntity<?> saveBidCollection(CollectionDetails newCollection);

    ResponseEntity<?> addBid(BidDetails bidDetails, Long userId);
}
