package com.CarBids.carBidsbiddingservice.controller;

import com.CarBids.carBidsbiddingservice.dto.BidDetails;
import com.CarBids.carBidsbiddingservice.dto.CollectionDTO;
import com.CarBids.carBidsbiddingservice.dto.CollectionDetails;
import com.CarBids.carBidsbiddingservice.dto.ResponseDTO;
import com.CarBids.carBidsbiddingservice.entity.BidCollection;
import com.CarBids.carBidsbiddingservice.enums.CollectionStatus;
import com.CarBids.carBidsbiddingservice.exception.exceptions.InvalidIdException;
import com.CarBids.carBidsbiddingservice.repository.BidCollectionRepository;
import com.CarBids.carBidsbiddingservice.service.BidService;
import com.CarBids.carBidsbiddingservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BidControllerTest {
    @InjectMocks
    private BidController bidController;

    @Mock
    private BidCollectionRepository bidCollectionRepository;
    @Mock
    private BidService bidService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void testCloseBidding() {
        Long collectionId = 1L;

        // Mock behavior of the bidService.closeBidding() method
        ResponseEntity<ResponseDTO> expectedResponse = ResponseEntity.ok(
                ResponseDTO.builder()
                        .status(HttpStatus.OK)
                        .message("Successfully placed bid")
                        .build()
        );
        Mockito.doReturn(expectedResponse)
                .when(bidService)
                .closeBidding(collectionId);

        // Call the controller method
        ResponseEntity<?> responseEntity = bidController.closeBidding(collectionId);

        // Verify that the expected ResponseEntity is returned
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully placed bid", ((ResponseDTO) responseEntity.getBody()).getMessage());

        // Verify that the bidService.closeBidding() method was called with the correct argument
        verify(bidService).closeBidding(collectionId);
    }


    @Test
    public void testGetCollection() {
        Long lotId = 123L;

        CollectionDTO mockCollection = CollectionDTO.builder()
                .noOfBids(2)
                .highestBidUserId(1L)
                .highestBidUsername("test")
                .currentHighestBid("1000")
                .build();

        when(bidService.getCollection(anyLong())).thenReturn(mockCollection);

        CollectionDTO result = bidController.getCollection(lotId);

        assertEquals(mockCollection.getNoOfBids(), result.getNoOfBids());
        assertEquals(mockCollection.getHighestBidUserId(), result.getHighestBidUserId());
        assertEquals(mockCollection.getCurrentHighestBid(), result.getCurrentHighestBid());
        assertEquals(mockCollection.getHighestBidUserId(), result.getHighestBidUserId());
        verify(bidService).getCollection(lotId);
    }

    @Test
    void testCreateLotCollection_Success() {
        // Given
        CollectionDetails collectionDetails = new CollectionDetails();
        when(bidService.saveBidCollection(collectionDetails)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // When
        ResponseEntity<?> response = bidController.createLotCollection(collectionDetails);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(bidService, times(1)).saveBidCollection(collectionDetails);
    }

    @Test
    void testSaveBidCollection_Success() {
        // Given
        CollectionDetails bidCollection = new CollectionDetails();
        when(bidCollectionRepository.save(any(BidCollection.class))).thenReturn(new BidCollection());

        // When
        ResponseEntity<?> response = bidService.saveBidCollection(bidCollection);

        // Then
        assertNull(response);
    }

    @Test
    public void testAddBidWithValidAuthorization() {
        // Mock the necessary behavior for valid authorization
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("123");

        // Mock the bidService behavior
        when(bidService.addBid(any(), anyLong())).thenReturn(
                new ResponseEntity<>( HttpStatus.CREATED)
        );

        // Create a sample BidDetails
        BidDetails bidDetails = new BidDetails(/* Initialize with required data */);

        // Call the controller method
        ResponseEntity<?> response = bidController.addBid(bidDetails, request);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(null, response.getBody());

        // Verify that methods were called as expected
        verify(jwtUtil).getUserIdFromToken("validToken");
        verify(bidService).addBid(bidDetails, 123L);
    }



}
