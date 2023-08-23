package com.CarBids.carBidsbiddingservice.repository;

import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BidCollectionRepositoryTest {
    @Mock
    private BidCollectionRepository bidCollectionRepository;



    @Test
    public void testFindOneByLotIdNotFound() {
        // Try to find a non-existing BidCollection by lotId
        BidCollection foundBidCollection = bidCollectionRepository.findOneBylotId(999L);

        assertNull(foundBidCollection);
    }


    @Test
    public void testFindOneByLotId() {
        // Mock data
        Long lotId = 1L;
        BidCollection mockBidCollection = BidCollection.builder()
                .collectionId(1L)
                .lotId(lotId)
                .currentHighestBid("100")
                .highestBidUserId(2L)
                .highestBidUsername("user1")
                .noOfBids(5)
                .biddingStatus(CollectionStatus.RUNNING)
                .startingValue("50")
                .startTimestamp(LocalDateTime.now())
                .endTimeStamp(LocalDateTime.now().plusHours(1))
                .build();
        when(bidCollectionRepository.findOneBylotId(lotId))
                .thenReturn(mockBidCollection);
        BidCollection foundBidCollection = bidCollectionRepository.findOneBylotId(lotId);
        assertNotNull(foundBidCollection);
        assertEquals(lotId, foundBidCollection.getLotId());
        verify(bidCollectionRepository, times(1)).findOneBylotId(lotId);

    }
}
