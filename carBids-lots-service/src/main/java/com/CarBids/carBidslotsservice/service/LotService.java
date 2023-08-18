package com.CarBids.carBidslotsservice.service;

import com.CarBids.carBidslotsservice.dto.CarDetails;
import com.CarBids.carBidslotsservice.entity.Lot;
import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import com.CarBids.carBidslotsservice.repository.LotRepository;
import com.CarBids.carBidslotsservice.specification.LotSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LotService implements ILotService {

    private final LotRepository lotRepository;

    public LotService(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    @Override
    public ResponseEntity<?> saveLot(CarDetails carDetails,Long userId) {
        TransmissionType transmissionType = TransmissionType.valueOf(carDetails.getTransmissionType().toUpperCase());
        BodyType bodyType = BodyType.valueOf(carDetails.getBodyType().toUpperCase());
        Lot newLot = Lot.builder()
                .vin(carDetails.getVin())
                .carName(carDetails.getCarName())
                .bodyType(bodyType)
                .transmissionType(transmissionType)
                .modelYear(carDetails.getModelYear())
                .photoUri(carDetails.getCarPhotosURI())
                .listerComment(carDetails.getListerComment())
                .startingValue(carDetails.getStartingValue())
                .minBidValue(carDetails.getMinBidValue())
                .lotStatus(LotStatus.RUNNING)
                .startTimestamp(LocalDateTime.now())
                .endDate(carDetails.getEndDate())
                .userId(userId)
                .build();
        lotRepository.save(newLot);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newLot);
    }

    @Override
    public ResponseEntity<?> getAllLot() {
        return new ResponseEntity<>(lotRepository.findAll(), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getFilteredLot(String modelYear, String transmissionType, String bodyType ) {
        Specification<Lot> specification = LotSpecification.withCriteria(modelYear, transmissionType, bodyType);
        List<Lot> filteredLot = lotRepository.findAll(specification);
        return new ResponseEntity<>(filteredLot,HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getActiveListings() {
        return new ResponseEntity<>(lotRepository.findByLotStatus(LotStatus.RUNNING),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getLotbyId(Long lotId) {
        return new ResponseEntity<>(lotRepository.findById(lotId).get(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> closeLotPremature(Long lotId) {
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot with ID " + lotId + " not found"));

        lot.setLotStatus(LotStatus.CLOSED);
        lotRepository.save(lot);
        return new ResponseEntity<>("successfully closed lot",HttpStatus.OK);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 3000)
    public void closeExpiredLots() {
        List<Lot> expiredLot = lotRepository.findByEndDateBeforeAndLotStatus(LocalDateTime.now(), LotStatus.RUNNING);
        expiredLot.stream()
                .filter(lot -> lot.getLotStatus() == LotStatus.RUNNING)
                .forEach(lot -> {
                    lot.setLotStatus(LotStatus.CLOSED);
                });
        lotRepository.saveAll(expiredLot);

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
