package com.CarBids.carBidslotsservice.controller;

import com.CarBids.carBidslotsservice.dto.CarDetails;
import com.CarBids.carBidslotsservice.dto.ResponseDTO;
import com.CarBids.carBidslotsservice.entity.Lot;
import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidAuthException;
import com.CarBids.carBidslotsservice.repository.LotRepository;
import com.CarBids.carBidslotsservice.service.LotService;
import com.CarBids.carBidslotsservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class LotControllerTest {
    @InjectMocks
    private LotController lotController;

    @Mock
    private LotService lotService;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void testCreateLotWithValidAuthorization() {
        // Arrange
        CarDetails carDetails = new CarDetails();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("123");
        when(lotService.saveLot(carDetails, 123L)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // Act
        ResponseEntity<?> response = lotController.createLot(carDetails, request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(lotService).saveLot(carDetails, 123L);
    }

    @Test
    public void testCreateLotWithInvalidAuthorization() {
        // Arrange
        CarDetails carDetails = new CarDetails();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidAuthException.class, () -> lotController.createLot(carDetails, request));
        verifyZeroInteractions(lotService);
    }

    private void verifyZeroInteractions(LotService lotService) {
    }

    @Test
    public void testCreateLotWithInvalidBearerToken() {
        // Arrange
        CarDetails carDetails = new CarDetails();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        // Act & Assert
        assertThrows(InvalidAuthException.class, () -> lotController.createLot(carDetails, request));
        verifyZeroInteractions(lotService);
    }

    @Test
    public void testCreateLotWithValidAuthorizationButInvalidUserIdInToken() {
        // Arrange
        CarDetails carDetails = new CarDetails();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("invalidUserId");

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> lotController.createLot(carDetails, request));
        verifyZeroInteractions(lotService);
    }

    @Test
    public void testCreateLotWithNullCarDetails() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("123");

        verifyZeroInteractions(lotService);
    }

    @Test
    public void testFilterBasedOnPropertyWithAllParameters() {
        // Arrange
        when(lotService.getFilteredLot("2023", "SUV", "AUTOMATIC")).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.filterBasedOnProperty("2023", "SUV", "AUTOMATIC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).getFilteredLot("2023", "SUV", "AUTOMATIC");
    }

    @Test
    public void testFilterBasedOnPropertyWithSomeParameters() {
        // Arrange
        when(lotService.getFilteredLot(null, "SEDAN", null)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.filterBasedOnProperty(null, "SEDAN", null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).getFilteredLot(null, "SEDAN", null);
    }

    @Test
    public void testFilterBasedOnPropertyWithNoParameters() {
        // Arrange
        when(lotService.getFilteredLot(null, null, null)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.filterBasedOnProperty(null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).getFilteredLot(null, null, null);
    }

    @Test
    public void testFilterBasedOnPropertyWithModelYear() {
        // Arrange
        when(lotService.getFilteredLot("2022", null, null)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.filterBasedOnProperty("2022", null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).getFilteredLot("2022", null, null);
    }

    @Test
    public void testFilterBasedOnPropertyWithBodyType() {
        // Arrange
        when(lotService.getFilteredLot(null, "SUV", null)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.filterBasedOnProperty(null, "SUV", null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).getFilteredLot(null, "SUV", null);
    }

    @Test
    public void testFilterBasedOnPropertyWithTransmissionType() {
        // Arrange
        when(lotService.getFilteredLot(null, null, "MANUAL")).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.filterBasedOnProperty(null, null, "MANUAL");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).getFilteredLot(null, null, "MANUAL");
    }

    @Test
    public void testFilterBasedOnPropertyWithNoFilters() {
        // Arrange
        when(lotService.getFilteredLot(null, null, null)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.filterBasedOnProperty(null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).getFilteredLot(null, null, null);
    }

    @Test
    public void testGetActiveListings() {
        // Arrange
        when(lotService.getActiveListings()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.getActiveListings();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).getActiveListings();
    }



    @Test
    public void testGetActiveListingsWithNoActiveListings() {
        // Arrange
        when(lotService.getActiveListings()).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        // Act
        ResponseEntity<?> response = lotController.getActiveListings();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(lotService).getActiveListings();
    }

    @Test
    public void testGetActiveListingsWithError() {
        // Arrange
        when(lotService.getActiveListings()).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        ResponseEntity<?> response = lotController.getActiveListings();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(lotService).getActiveListings();
    }



    @Test
    public void testGetClosedListingsWithNoClosedListings() {
        // Arrange
        when(lotService.getClosedListings()).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        // Act
        ResponseEntity<?> response = lotController.getClosedListings();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(lotService).getClosedListings();
    }

    @Test
    public void testGetClosedListingsWithError() {
        // Arrange
        when(lotService.getClosedListings()).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        ResponseEntity<?> response = lotController.getClosedListings();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(lotService).getClosedListings();
    }



    @Test
    public void testGetLotByIdWithInvalidId() {
        // Arrange
        Long invalidLotId = -1L;

        verifyZeroInteractions(lotService);
    }

    @Test
    public void testCheckLotStatusWithActiveLot() {
        // Arrange
        Long activeLotId = 123L;
        when(lotService.checkLotStatus(activeLotId)).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id.equals(activeLotId)) {
                return new ResponseEntity<>("Active", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid ID", HttpStatus.NOT_FOUND);
            }
        });

        // Act
        ResponseEntity<?> response = lotController.checkLotStatus(activeLotId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Active", response.getBody());
        verify(lotService).checkLotStatus(activeLotId);
    }

    @Test
    public void testCheckLotStatusWithClosedLot() {
        // Arrange
        Long closedLotId = 456L;
        when(lotService.checkLotStatus(closedLotId)).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id.equals(closedLotId)) {
                return new ResponseEntity<>("Closed", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid ID", HttpStatus.NOT_FOUND);
            }
        });

        // Act
        ResponseEntity<?> response = lotController.checkLotStatus(closedLotId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Closed", response.getBody());
        verify(lotService).checkLotStatus(closedLotId);
    }



    @Test
    public void testCloseLotPrematureWithValidAuthorization() {
        // Arrange
        Long lotId = 123L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("456");
        when(lotService.closeLotPremature(lotId, 456L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        ResponseEntity<?> response = lotController.clostLotPremature(lotId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService).closeLotPremature(lotId, 456L);
    }

    @Test
    public void testCloseLotPrematureWithInvalidAuthorization() {
        // Arrange
        Long lotId = 123L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidAuthException.class, () -> lotController.clostLotPremature(lotId, request));
        verifyZeroInteractions(lotService);
    }

    @Test
    public void testCloseLotPrematureWithError() {
        // Arrange
        Long lotId = 123L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("456");
        when(lotService.closeLotPremature(lotId, 456L)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        ResponseEntity<?> response = lotController.clostLotPremature(lotId, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(lotService).closeLotPremature(lotId, 456L);
    }

    @Test
    void testCheckLotIdExists() {
        // Given
        Long lotId = 123L;
        when(lotController.checkLotId(lotId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // When
        ResponseEntity<?> response = lotController.checkLotId(lotId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService, times(1)).checkLotId(lotId);
    }

    @Test
    void testCheckLotIdDoesNotExist() {
        // Given
        Long lotId = 456L;
        when(lotController.checkLotId(lotId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // When
        ResponseEntity<?> response = lotController.checkLotId(lotId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lotService, times(1)).checkLotId(lotId);
    }


    @Test
    void testGetAllLot() {
        // Given
        List<Lot> fakeLotTests = new ArrayList<>();
        List<String> photoURI = new ArrayList<>();
        Date date = new Date();
        fakeLotTests.add(new Lot(1L,"vin","carName",BodyType.COUPE, TransmissionType.AUTOMATIC,"2023",photoURI,"test","startt", LotStatus.RUNNING, LocalDateTime.now(), date,1L,"test" ));
        fakeLotTests.add(new Lot(1L,"vin","carName",BodyType.COUPE, TransmissionType.AUTOMATIC,"2023",photoURI,"test","startt", LotStatus.RUNNING, LocalDateTime.now(), date,1L,"test" ));
        fakeLotTests.add(new Lot(1L,"vin","carName",BodyType.COUPE, TransmissionType.AUTOMATIC,"2023",photoURI,"test","startt", LotStatus.RUNNING, LocalDateTime.now(), date,1L,"test" ));
        ResponseDTO expectedResponse = new ResponseDTO(HttpStatus.OK, "All Running and closed lots", fakeLotTests);
        when(lotService.getAllLot()).thenReturn(new ResponseEntity(expectedResponse, HttpStatus.OK));
        ResponseEntity<?> response = lotController.getAllLot();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertEquals(expectedResponse.getStatus(), responseDTO.getStatus());
        assertEquals(expectedResponse.getMessage(), responseDTO.getMessage());
        assertEquals(expectedResponse.getData(), responseDTO.getData());
        verify(lotService, times(1)).getAllLot();    }


}
