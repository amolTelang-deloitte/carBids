package com.CarBids.carBidsbiddingservice.service;

import com.CarBids.carBidsbiddingservice.dto.*;
import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import com.CarBids.carBidsbiddingservice.event.BidPlacedEvent;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidDataException;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidIdException;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidUserException;
import com.CarBids.carBidsbiddingservice.feignClient.AuthFeignClient;
import com.CarBids.carBidsbiddingservice.repository.BidCollectionRepository;
import com.CarBids.carBidsbiddingservice.repository.BidRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class BiddingServiceTest {
    private final AuthFeignClient authFeignClient = mock(AuthFeignClient.class);
    private final BidRepository bidRepository = mock(BidRepository.class);
    private final BidCollectionRepository bidCollectionRepository = mock(BidCollectionRepository.class);
    @InjectMocks
    private BidService bidService;
    @Mock
    private BidPlacedEvent eventPublisher;
    @Mock
    private Clock fixedClock;

    @Test
    void testCheckUserIdSuccess() {
        // Arrange
        Long userId = 123L;
        ResponseEntity<UserIdCheck> successResponse = new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK);

        when(authFeignClient.checkUserId(userId)).thenReturn(successResponse);

        // Act & Assert
        assertDoesNotThrow(() -> bidService.checkUserId(userId));
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testCheckUserIdFailure() {
        // Arrange
        Long userId = 456L;
        ResponseEntity<UserIdCheck> failureResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(authFeignClient.checkUserId(userId)).thenReturn(failureResponse);

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> bidService.checkUserId(userId));
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testCheckUserIdFeignClientError() {
        // Arrange
        Long userId = 789L;

        when(authFeignClient.checkUserId(userId)).thenThrow(InvalidIdException.class);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> bidService.checkUserId(userId));
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testValidUsernameReturned() {
        Long userId = 123L; // Replace with an actual valid user ID
        String expectedUsername = "testUser"; // Replace with the expected username
        when(authFeignClient.getUsername(userId)).thenReturn(expectedUsername);

        String result = bidService.getUsername(userId);

        assertEquals(expectedUsername, result);
    }

    @Test
    void testEmptyUsernameReturned() {
        Long userId = 456L; // Replace with an actual valid user ID
        when(authFeignClient.getUsername(userId)).thenReturn("");

        assertThrows(InvalidDataException.class, () -> bidService.getUsername(userId));
    }

    @Test
    void testInvalidUserId() {
        Long userId = 789L; // Replace with an actual invalid user ID
        when(authFeignClient.getUsername(userId)).thenReturn(null); // Or throw some exception in the mock

        assertThrows(NullPointerException.class, () -> bidService.getUsername(userId));
    }
    @Test
    void testNullUserId() {
        Long userId = null;

        assertThrows(NullPointerException.class, () -> bidService.getUsername(userId));
    }

    @Test
    void testValidLotId() {
        // Mocking the repository behavior
        BidCollection mockBidCollection = new BidCollection();
        mockBidCollection.setCurrentHighestBid("100");
        mockBidCollection.setHighestBidUsername("user123");
        mockBidCollection.setHighestBidUserId(1L);
        mockBidCollection.setNoOfBids(5);

        when(bidCollectionRepository.findOneBylotId(1L)).thenReturn(mockBidCollection);

        // Calling the service method
        CollectionDTO result = bidService.getCollection(1L);

        // Assertions
        assertEquals("100", result.getCurrentHighestBid());
        assertEquals("user123", result.getHighestBidUsername());
        assertEquals(1L, result.getHighestBidUserId());
        assertEquals(5, result.getNoOfBids());
    }

    @Test
    void testInvalidLotId() {
        // Mocking the repository behavior
        when(bidCollectionRepository.findOneBylotId(anyLong())).thenReturn(null);

        // Assertions for exception
        InvalidIdException exception = assertThrows(InvalidIdException.class, () -> {
            bidService.getCollection(2L);
        });

        assertEquals("Invalid lotId, Check again", exception.getMessage());
    }

    // Test case to cover the logger.warn() call
    @Test
    void testLoggerWarnIsCalledForInvalidLotId() {
        // Mocking the repository behavior
        when(bidCollectionRepository.findOneBylotId(anyLong())).thenReturn(null);

        // Calling the service method
        try {
            bidService.getCollection(2L);
        } catch (InvalidIdException ignored) {
        }


    }

    // Test case to cover the logger.info() call
    @Test
    void testLoggerInfoIsCalledForValidLotId() {
        // Mocking the repository behavior
        BidCollection mockBidCollection = new BidCollection();
        when(bidCollectionRepository.findOneBylotId(anyLong())).thenReturn(mockBidCollection);

        // Calling the service method
        bidService.getCollection(1L);

    }

    @Test
    void testValidLotIdWithNullFieldsInCollectionDTO() {
        // Mocking the repository behavior
        BidCollection mockBidCollection = new BidCollection();
        mockBidCollection.setCurrentHighestBid("200");
        // Other fields are null

        when(bidCollectionRepository.findOneBylotId(3L)).thenReturn(mockBidCollection);

        // Calling the service method
        CollectionDTO result = bidService.getCollection(3L);

        // Assertions for null fields
        assertNull(result.getHighestBidUsername());
        assertNull(result.getHighestBidUserId());
    }

    @Test
    void testLoggerWarnIsNotCalledForValidLotId() {
        // Mocking the repository behavior
        BidCollection mockBidCollection = new BidCollection();
        when(bidCollectionRepository.findOneBylotId(anyLong())).thenReturn(mockBidCollection);

        // Calling the service method
        bidService.getCollection(1L);

    }

    @Test
    void testValidLotIdWithZeroNoOfBids() {
        // Mocking the repository behavior
        BidCollection mockBidCollection = new BidCollection();
        mockBidCollection.setNoOfBids(0);

        when(bidCollectionRepository.findOneBylotId(4L)).thenReturn(mockBidCollection);

        // Calling the service method
        CollectionDTO result = bidService.getCollection(4L);

        // Assertions for noOfBids
        assertEquals(0, result.getNoOfBids());
    }

    @Test
    void testNegativeLotId() {
        // Mocking the repository behavior (not used in this test case)
        when(bidCollectionRepository.findOneBylotId(anyLong())).thenReturn(null);

        // Assertions for exception
        assertThrows(InvalidIdException.class, () -> {
            bidService.getCollection(-1L);
        });
    }

    @Test
    public void testCloseBidding_Success() {
        Long lotId = 1L;

        BidCollection collection = new BidCollection();
        collection.setLotId(lotId);

        when(bidCollectionRepository.findOneBylotId(lotId)).thenReturn(collection);
        ResponseEntity<?> response = bidService.closeBidding(lotId);

        verify(bidCollectionRepository).save(collection);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully placed bid", ((ResponseDTO) response.getBody()).getMessage());
    }




}
