package com.CarBids.carBidsbiddingservice.repository;

import com.CarBids.carBidsbiddingservice.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidCollectionRepository extends JpaRepository<Bid,Long> {
    List<Bid> findBycollectionIdOrderByBidValueDesc(Long collectionId);
}
