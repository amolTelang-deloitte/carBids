package com.CarBids.carBidslotsservice.service;

import com.CarBids.carBidslotsservice.dto.CarDetails;
import org.springframework.http.ResponseEntity;

public interface ILotService {

    ResponseEntity<?> saveLot(CarDetails carDetails,Long userId);
    ResponseEntity<?>getFilteredLot(String modelYear, String bodyType, String transmissionType );
    ResponseEntity<?> deleteLot(Long lotId);
    ResponseEntity<?> updateLot(Long lotId);


}
