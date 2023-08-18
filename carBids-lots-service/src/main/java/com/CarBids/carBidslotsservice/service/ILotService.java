package com.CarBids.carBidslotsservice.service;

import com.CarBids.carBidslotsservice.dto.CarDetails;
import org.springframework.http.ResponseEntity;

public interface ILotService {

    ResponseEntity<?> saveLot(CarDetails carDetails,Long userId);
    ResponseEntity<?>getAllLot();
    ResponseEntity<?>getFilteredLot(String modelYear, String bodyType, String transmissionType );
    ResponseEntity<?>getActiveListings();
    ResponseEntity<?>getLotbyId(Long lotId);
    ResponseEntity<?>closeLotPremature(Long lotId);
    ResponseEntity<?> deleteLot(Long lotId);
    ResponseEntity<?> updateLot(Long lotId);


}
