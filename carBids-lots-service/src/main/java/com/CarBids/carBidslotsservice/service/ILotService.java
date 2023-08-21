package com.CarBids.carBidslotsservice.service;

import com.CarBids.carBidslotsservice.dto.CarDetails;
import com.CarBids.carBidslotsservice.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

public interface ILotService {

    ResponseEntity<?> saveLot(CarDetails carDetails,Long userId);
    ResponseEntity<?>getAllLot();
    ResponseEntity<?>getFilteredLot(String modelYear, String bodyType, String transmissionType );
    ResponseEntity<?>getActiveListings();
    ResponseEntity<?>getLotbyId(Long lotId);
    ResponseEntity<?>closeLotPremature(Long lotId,Long userId);

    ResponseEntity<?>checkLotId(Long lotId);
    ResponseEntity<?>checkLotStatus(Long lotId);
    ResponseEntity<?>getClosedListings();
    ResponseDTO checkUserId(Long id);
   // void closeExpiredLots();



}
