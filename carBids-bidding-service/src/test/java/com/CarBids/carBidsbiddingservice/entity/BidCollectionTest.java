package com.CarBids.carBidsbiddingservice.entity;
import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;
public class BidCollectionTest {
    @Test
    public void testCollectionIdGetterAndSetter() {
        BidCollection bidCollection = new BidCollection();
        bidCollection.setCollectionId(1L);
        assertEquals(1L, bidCollection.getCollectionId());
    }

    @Test
    public void testLotIdGetterAndSetter() {
        BidCollection bidCollection = new BidCollection();
        bidCollection.setLotId(2L);
        assertEquals(2L, bidCollection.getLotId());
    }

    @Test
    public void testCurrentHighestBidGetterAndSetter() {
        BidCollection bidCollection = new BidCollection();
        bidCollection.setCurrentHighestBid("150");
        assertEquals("150", bidCollection.getCurrentHighestBid());
    }

    @Test
    public void testHighestBidUserIdGetterAndSetter() {
        BidCollection bidCollection = new BidCollection();
        bidCollection.setHighestBidUserId(3L);
        assertEquals(3L, bidCollection.getHighestBidUserId());
    }

    @Test
    public void testHighestBidUsernameGetterAndSetter() {
        BidCollection bidCollection = new BidCollection();
        bidCollection.setHighestBidUsername("testuser");
        assertEquals("testuser", bidCollection.getHighestBidUsername());
    }

    @Test
    public void testNoOfBidsGetterAndSetter() {
        BidCollection bidCollection = new BidCollection();
        bidCollection.setNoOfBids(5);
        assertEquals(5, bidCollection.getNoOfBids());
    }

    @Test
    public void testBiddingStatusGetterAndSetter() {
        BidCollection bidCollection = new BidCollection();
        bidCollection.setBiddingStatus(CollectionStatus.RUNNING);
        assertEquals(CollectionStatus.RUNNING, bidCollection.getBiddingStatus());


    }

    @Test
    public void testStartingValueGetterAndSetter() {
        BidCollection bidCollection = new BidCollection();
        bidCollection.setStartingValue("100");
        assertEquals("100", bidCollection.getStartingValue());
    }

    @Test
    public void testStartTimestampGetterAndSetter() {
        LocalDateTime startTimestamp = LocalDateTime.now();
        BidCollection bidCollection = new BidCollection();
        bidCollection.setStartTimestamp(startTimestamp);
        assertEquals(startTimestamp, bidCollection.getStartTimestamp());
    }

    @Test
    public void testEndTimeStampGetterAndSetter() {
        LocalDateTime endTimestamp = LocalDateTime.now().plusHours(1);
        BidCollection bidCollection = new BidCollection();
        bidCollection.setEndTimeStamp(endTimestamp);
        assertEquals(endTimestamp, bidCollection.getEndTimeStamp());
    }

    @Test
    public void testNoArgsConstructor() {
        BidCollection bidCollection = new BidCollection();
        assertNotNull(bidCollection);
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime startTimestamp = LocalDateTime.now();
        LocalDateTime endTimestamp = LocalDateTime.now().plusHours(1);
        BidCollection bidCollection = new BidCollection(1L, 2L, "150", 3L, "testuser", 5, CollectionStatus.RUNNING, "100", startTimestamp, endTimestamp);
        assertNotNull(bidCollection);
        assertEquals(1L, bidCollection.getCollectionId());
        assertEquals(2L, bidCollection.getLotId());
        assertEquals("150", bidCollection.getCurrentHighestBid());
        assertEquals(3L, bidCollection.getHighestBidUserId());
        assertEquals("testuser", bidCollection.getHighestBidUsername());
        assertEquals(5, bidCollection.getNoOfBids());
        assertEquals(CollectionStatus.RUNNING, bidCollection.getBiddingStatus());
        assertEquals("100", bidCollection.getStartingValue());
        assertEquals(startTimestamp, bidCollection.getStartTimestamp());
        assertEquals(endTimestamp, bidCollection.getEndTimeStamp());
    }

    @Test
    public void testToString() {
        LocalDateTime startTimestamp = LocalDateTime.now();
        LocalDateTime endTimestamp = LocalDateTime.now().plusHours(1);
        BidCollection bidCollection = new BidCollection(1L, 2L, "150", 3L, "testuser", 5, CollectionStatus.RUNNING, "100", startTimestamp, endTimestamp);
        String expectedToString = "BidCollection(collectionId=1, lotId=2, currentHighestBid=150, highestBidUserId=3, " +
                "highestBidUsername=testuser, noOfBids=5, biddingStatus=RUNNING, startingValue=100, startTimestamp=" + startTimestamp +
                ", endTimeStamp=" + endTimestamp + ")";
        assertEquals(expectedToString, bidCollection.toString());
    }
}
