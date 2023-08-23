package com.CarBids.carBidsbiddingservice.entity;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;



public class BidTest {

    @Test
    public void testIdGetterAndSetter() {
        Bid bid = new Bid();
        bid.setId(1L);
        assertEquals(1L, bid.getId());
    }

    @Test
    public void testCollectionIdGetterAndSetter() {
        Bid bid = new Bid();
        bid.setCollectionId(2L);
        assertEquals(2L, bid.getCollectionId());
    }

    @Test
    public void testUserIdGetterAndSetter() {
        Bid bid = new Bid();
        bid.setUserId(3L);
        assertEquals(3L, bid.getUserId());
    }

    @Test
    public void testUserNameGetterAndSetter() {
        Bid bid = new Bid();
        bid.setUserName("testuser");
        assertEquals("testuser", bid.getUserName());
    }

    @Test
    public void testBidValueGetterAndSetter() {
        Bid bid = new Bid();
        bid.setBidValue("100");
        assertEquals("100", bid.getBidValue());
    }

    @Test
    public void testNoArgsConstructor() {
        Bid bid = new Bid();
        assertNotNull(bid);
    }

    @Test
    public void testAllArgsConstructor() {
        Bid bid = new Bid(1L, 2L, 3L, "testuser", "100");
        assertNotNull(bid);
        assertEquals(1L, bid.getId());
        assertEquals(2L, bid.getCollectionId());
        assertEquals(3L, bid.getUserId());
        assertEquals("testuser", bid.getUserName());
        assertEquals("100", bid.getBidValue());
    }

    @Test
    public void testToString() {
        Bid bid = new Bid(1L, 2L, 3L, "testuser", "100");
        String expectedToString = "Bid(id=1, collectionId=2, userId=3, userName=testuser, bidValue=100)";
        assertEquals(expectedToString, bid.toString());
    }
}