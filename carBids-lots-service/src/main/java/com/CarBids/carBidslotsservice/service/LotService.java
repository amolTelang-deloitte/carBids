package com.CarBids.carBidslotsservice.service;

import com.CarBids.carBidslotsservice.dto.CarDetails;
import org.springframework.http.ResponseEntity;

public class LotService implements ILotService {
    @Override
    public ResponseEntity<?> saveLot(CarDetails carDetails) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteLot(Long lotId) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateLot(Long lotId) {
        return null;
    }
}
