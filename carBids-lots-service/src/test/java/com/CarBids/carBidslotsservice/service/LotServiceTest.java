package com.CarBids.carBidslotsservice.service;

import com.CarBids.carBidslotsservice.dto.BidCollection;
import com.CarBids.carBidslotsservice.dto.UserIdCheck;
import com.CarBids.carBidslotsservice.entity.Lot;
import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidDataException;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidIdException;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidUserException;
import com.CarBids.carBidslotsservice.feignClient.AuthFeignClient;
import com.CarBids.carBidslotsservice.feignClient.BiddingFeignClient;
import com.CarBids.carBidslotsservice.repository.LotRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LotServiceTest {

    @Mock
    private AuthFeignClient authFeignClient;
    @InjectMocks
    private LotService lotService;

    private LotRepository lotRepository = mock(LotRepository.class);
    @Mock
    private BiddingFeignClient biddingFeignClient;

    @Test
    void testCheckUserIdSuccess() {
        // Arrange
        Long userId = 123L;
        UserIdCheck userIdCheck = new UserIdCheck(true); // Replace this with your UserIdCheck constructor
        ResponseEntity<UserIdCheck> successfulResponse = new ResponseEntity<>(userIdCheck, HttpStatus.OK);

        when(authFeignClient.checkUserId(userId)).thenReturn(successfulResponse);

        // Act
        Boolean result = lotService.checkUserId(userId);

        // Assert
        assertTrue(result);
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testCheckUserIdFailure() {
        // Arrange
        Long userId = 456L;
        ResponseEntity<UserIdCheck> failureResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(authFeignClient.checkUserId(userId)).thenReturn(failureResponse);

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> lotService.checkUserId(userId));
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testCheckUserIdNullResponse() {
        // Arrange
        Long userId = 1l;
        ResponseEntity<UserIdCheck> nullResponse = null;

        when(authFeignClient.checkUserId(userId)).thenReturn(nullResponse);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> lotService.checkUserId(userId));
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testCheckLotStatusClosed() {
        Long lotId = 1L;
        Lot lot = new Lot();
        lot.setLotStatus(LotStatus.CLOSED);

        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));

        ResponseEntity<?> response = lotService.checkLotStatus(lotId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    void testCheckLotStatusOpen() {
        Long lotId = 2L;
        Lot lot = new Lot();
        lot.setLotStatus(LotStatus.RUNNING);

        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));

        ResponseEntity<?> response = lotService.checkLotStatus(lotId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    void testCheckLotStatusInvalid() {
        Long lotId = 3L;

        when(lotRepository.findById(lotId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            lotService.checkLotStatus(lotId);
        });
    }

    @Test
    void testExistingLotId() {
        Long existingLotId = 1L;

        when(lotRepository.existsById(existingLotId)).thenReturn(true);

        ResponseEntity<?> response = lotService.checkLotId(existingLotId);

        verify(lotRepository, times(1)).existsById(existingLotId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody());
    }

    @Test
    void testNonExistingLotId() {
        Long nonExistingLotId = 2L;

        when(lotRepository.existsById(nonExistingLotId)).thenReturn(false);

        ResponseEntity<?> response = lotService.checkLotId(nonExistingLotId);

        verify(lotRepository, times(1)).existsById(nonExistingLotId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse((Boolean) response.getBody());
    }

    @Test
    void testValidUsernameReturned() {
        Long userId = 123L; // Replace with an actual valid user ID
        String expectedUsername = "testUser"; // Replace with the expected username
        when(authFeignClient.getUsername(userId)).thenReturn(expectedUsername);

        String result = lotService.getUsername(userId);

        assertEquals(expectedUsername, result);
    }

    @Test
    void testEmptyUsernameReturned() {
        Long userId = 456L; // Replace with an actual valid user ID
        when(authFeignClient.getUsername(userId)).thenReturn("");

        assertThrows(InvalidDataException.class, () -> lotService.getUsername(userId));
    }

    @Test
    void testInvalidUserId() {
        Long userId = 789L; // Replace with an actual invalid user ID
        when(authFeignClient.getUsername(userId)).thenReturn(null); // Or throw some exception in the mock

        assertThrows(NullPointerException.class, () -> lotService.getUsername(userId));
    }
    @Test
    void testNullUserId() {
        Long userId = null;

        assertThrows(NullPointerException.class, () -> lotService.getUsername(userId));
    }

    @Test
    void testValidYear() {

        // Test with a valid year
        assertTrue(lotService.checkValidYear("2023"));
    }

    @Test
    void testEmptyYear() {
        // Test with an empty year
        assertFalse(lotService.checkValidYear(""));
    }
    @Test
    void testCurrentYear() {

        // Test with the current year
        int currentYear = LocalDate.now().getYear();
        assertTrue(lotService.checkValidYear(Integer.toString(currentYear)));
    }

    @Test
    void testInvalidYear() {

        // Test with an invalid year (string that's not a number)
        assertFalse(lotService.checkValidYear("2222"));
    }


    @Test
    public void testGetClosedListings() {

        when(lotRepository.findByLotStatus(LotStatus.CLOSED))
                .thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = lotService.getClosedListings();

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        // You can also check the content of the ResponseEntity if needed

        // Verify interactions with the mock
        verify(lotRepository, times(1)).findByLotStatus(LotStatus.CLOSED);
        verifyNoMoreInteractions(lotRepository);
    }

    @Test
    public void testGetClosedListings_NoClosedLots() {
        // Mock the dependencies
        when(lotRepository.findByLotStatus(LotStatus.CLOSED))
                .thenReturn(Collections.emptyList()); // No closed lots


        // Call the method
        ResponseEntity<?> responseEntity = lotService.getClosedListings();

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        // You might want to check the content of the ResponseEntity if it's expected to be empty

        // Verify interactions with the mock
        verify(lotRepository, times(1)).findByLotStatus(LotStatus.CLOSED);
        verifyNoMoreInteractions(lotRepository);
    }

    @Test
    public void testCloseLotPremature_Success() {
        // Arrange
        Long lotId = 1L;
        Long userId = 123L;
        Lot lot = new Lot();
        lot.setUserId(userId);
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));

        // Act
        ResponseEntity<?> response = lotService.closeLotPremature(lotId, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotRepository).save(eq(lot));
        verify(biddingFeignClient).closeBiddingService(eq(lotId));
    }

    @Test
    public void testCloseLotPremature_LotNotFound() {
        // Arrange
        Long lotId = 1L;
        Long userId = 123L;
        when(lotRepository.findById(lotId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(InvalidIdException.class, () -> lotService.closeLotPremature(lotId, userId));
        verify(lotRepository, never()).save(any());
        verify(biddingFeignClient, never()).closeBiddingService(anyLong());
    }

    @Test
    public void testCloseLotPremature_InvalidUser() {
        // Arrange
        Long lotId = 1L;
        Long userId = 123L;
        Lot lot = new Lot();
        lot.setUserId(userId + 1); // User ID mismatch
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));

        // Act and Assert
        assertThrows(InvalidUserException.class, () -> lotService.closeLotPremature(lotId, userId));
        verify(lotRepository, never()).save(any());
        verify(biddingFeignClient, never()).closeBiddingService(anyLong());
    }
    @Test
    void testGetLotById_ValidId() {
        Long lotId = 1L;
        List<String> uris = new ArrayList<>();
        uris.add("https://example.com/photo.jpg");
        uris.add("https://example.com/photo.jpg");
        uris.add("https://example.com/photo.jpg");
        uris.add("https://example.com/photo.jpg");
        uris.add("https://example.com/photo.jpg");
        uris.add("https://example.com/photo.jpg");
        uris.add("https://example.com/photo.jpg");
        Date testDate = new Date();
        int year = 2023;
        int month = 8; // August
        int day = 21;
        int hour = 12;
        int minute = 30;
        int second = 0;

        LocalDateTime test = LocalDateTime.of(year, month, day, hour, minute, second);

        Lot lot = new Lot(lotId, "1234567890", "Honda Civic", BodyType.SEDAN, TransmissionType.AUTOMATIC, "2023", uris, "This is a great car!", "10000", LotStatus.RUNNING, test ,testDate, 1L, "johndoe");
        BidCollection bidCollection = new BidCollection("1000", 1L, "johndoe", 1);

        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));
        when(biddingFeignClient.getBidCollection(lotId)).thenReturn(bidCollection);


       Optional <Lot> response = lotRepository.findById(lotId);

        assertEquals(
                lotId,
                response.get().getLotId()
        );
        assertEquals(
                "1234567890",
                response.get().getVin()
        );
        assertEquals(
                "Honda Civic",
                response.get().getCarName()
        );
        assertEquals(
                BodyType.SEDAN,
                response.get().getBodyType()
        );
        assertEquals(
                TransmissionType.AUTOMATIC,
                response.get().getTransmissionType()
        );
        assertEquals(
                "2023",
                response.get().getModelYear()
        );
        assertEquals(
                uris,
                response.get().getPhotoUri()
        );
        assertEquals(
                "This is a great car!",
                response.get().getListerComment()
        );
        assertEquals(
                "10000",
                response.get().getStartingValue()
        );
        assertEquals(
                LotStatus.RUNNING,
                response.get().getLotStatus()
        );
        assertEquals(
                test,
                response.get().getStartTimestamp()
        );
        assertEquals(
                testDate,
                response.get().getEndDate()
        );
        assertEquals(
                1L,
                response.get().getUserId()
        );
        assertEquals(
                "johndoe",
                response.get().getUsername()
        );

    }



}
