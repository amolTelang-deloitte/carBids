package com.CarBids.carBidsbiddingservice.repository;

import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BidCollectionRepositoryTest {
    @Autowired
    private BidCollectionRepository bidCollectionRepository;

    @Test
    public void testFindOneByLotId() {
        // Create a sample BidCollection
        BidCollection bidCollection = new BidCollection();
        bidCollection.setLotId(1L);
        bidCollection.setBiddingStatus(CollectionStatus.RUNNING);
        bidCollection.setStartingValue("100");
        bidCollection.setStartTimestamp(LocalDateTime.now());
        bidCollectionRepository.save(bidCollection);

        // Find the BidCollection by lotId
        BidCollection foundBidCollection = bidCollectionRepository.findOneByLotId(1L);

        assertNotNull(foundBidCollection);
        assertEquals(1L, foundBidCollection.getLotId().longValue());
    }

    @Test
    public void testFindOneByLotIdNotFound() {
        // Try to find a non-existing BidCollection by lotId
        BidCollection foundBidCollection = bidCollectionRepository.findOneByLotId(999L);

        assertNull(foundBidCollection);
    }
}
