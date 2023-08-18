package com.CarBids.carBidsbiddingservice.repository;

import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidCollectionRepository extends JpaRepository<BidCollection,Long> {

    BidCollection findOneBylotId(Long collectionId);
}
