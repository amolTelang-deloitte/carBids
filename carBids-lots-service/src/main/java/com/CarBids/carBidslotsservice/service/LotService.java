package com.CarBids.carBidslotsservice.service;

import com.CarBids.carBidslotsservice.dto.*;
import com.CarBids.carBidslotsservice.entity.Lot;
import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidDataException;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidIdException;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidTypeException;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidUserException;
import com.CarBids.carBidslotsservice.feignClient.AuthFeignClient;
import com.CarBids.carBidslotsservice.feignClient.BiddingFeignClient;
import com.CarBids.carBidslotsservice.repository.LotRepository;
import com.CarBids.carBidslotsservice.specification.LotSpecification;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class LotService implements ILotService {
    private final static Logger logger = LoggerFactory.getLogger(LotService.class);

    private final LotRepository lotRepository;

    private final AuthFeignClient authFeignClient;

    private final BiddingFeignClient biddingFeignClient;

    @Autowired
    public LotService(LotRepository lotRepository, AuthFeignClient authFeignClient, BiddingFeignClient biddingFeignClient) {
        this.lotRepository = lotRepository;
        this.authFeignClient = authFeignClient;
        this.biddingFeignClient = biddingFeignClient;
    }

    @Override
    @HystrixCommand(groupKey = "bidding", commandKey = "startBidding",fallbackMethod = "biddingServiceFallback")
    public ResponseEntity<?> saveLot(CarDetails carDetails,Long userId) {

       if(!Arrays.stream(TransmissionType.values())
               .map(Enum::name)
               .anyMatch(transmissionType -> transmissionType.equals(carDetails.getTransmissionType().toUpperCase()))){
           logger.warn("Invalid Transmision type entered"+" "+LocalDateTime.now());
           throw new InvalidTypeException("Invalid Transmission Type, Check again");
       }



       if(!Arrays.stream(BodyType.values())
               .map(Enum::name)
               .anyMatch(bodyType -> bodyType.equals(carDetails.getBodyType().toUpperCase()))){
           logger.warn("Invalid Body type entered"+" "+LocalDateTime.now());
           throw new InvalidTypeException("Invalid Body Type, Check again");
       }


       if(!checkValidYear(carDetails.getModelYear())){
           logger.warn("Invalid model year entered"+" "+LocalDateTime.now());
           throw new InvalidDataException("Invalid Model Year. Check again");
       }

       if(carDetails.getCarPhotosURI().size()< 6){
           logger.warn("Added less than 6 photos"+" "+LocalDateTime.now());
           throw new InvalidDataException("Please upload a minimum of 6 photos");
       }

       if(Integer.parseInt(carDetails.getStartingValue()) <= 0){
           logger.warn("Entered negative or null starting value"+" "+LocalDateTime.now());
           throw new InvalidDataException("Please give valid starting price");
       }

       if(!checkUserId(userId)){
           logger.error("Invalid Credentials entered"+" "+LocalDateTime.now());
           throw new InvalidDataException("Invalid Credential, Login again");
       }

        TransmissionType transmissionType = TransmissionType.valueOf(carDetails.getTransmissionType().toUpperCase());
        BodyType bodyType = BodyType.valueOf(carDetails.getBodyType().toUpperCase());
        String username = getUsername(userId);

        Lot newLot = Lot.builder()
                .vin(carDetails.getVin())
                .carName(carDetails.getCarName())
                .bodyType(bodyType)
                .transmissionType(transmissionType)
                .modelYear(carDetails.getModelYear())
                .photoUri(carDetails.getCarPhotosURI())
                .listerComment(carDetails.getListerComment())
                .startingValue(carDetails.getStartingValue())
                .lotStatus(LotStatus.RUNNING)
                .startTimestamp(LocalDateTime.now())
                .endDate(carDetails.getEndDate())
                .userId(userId)
                .username(username)
                .build();
        logger.info("successfully saved lot in database "+" "+LocalDateTime.now());
       Lot savedLot =  lotRepository.save(newLot);
       try{
           CollectionDetails collectionDetails = CollectionDetails.builder()
                   .lotId(savedLot.getLotId())
                   .startingValue(savedLot.getStartingValue())
                   .build();

           logger.info("successfully started LotCollection in database "+" "+LocalDateTime.now());
           biddingFeignClient.startBiddingService(collectionDetails);
       }catch (Exception e){
           logger.error("Bidding Service down"+" "+LocalDateTime.now());
           throw new RuntimeException("bidding service down");
       }
        logger.info("successfully saved Lot in database and started Lot collection in Bidding service"+" "+LocalDateTime.now());
        return new ResponseEntity<>(newLot,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getAllLot() {
        logger.info("Getting all lot from the database "+" "+LocalDateTime.now());
        return new ResponseEntity<>(lotRepository.findAll(), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getFilteredLot(String modelYear, String transmissiontype, String bodytype ) {
        logger.info("Attempting to filter Lots "+" "+LocalDateTime.now());
        if(!checkValidYear(modelYear)){
            logger.warn("Invalid model year entered "+" "+LocalDateTime.now());
            throw new InvalidDataException("Invalid Model Year. Check again");
        }
        Specification<Lot> specification = LotSpecification.withCriteria(modelYear, transmissiontype, bodytype);
        List<Lot> filteredLot = lotRepository.findAll(specification);
        return new ResponseEntity<>(filteredLot,HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getActiveListings() {
        logger.info("Attempting to find all Active Lots "+" "+LocalDateTime.now());
        return new ResponseEntity<>(lotRepository.findByLotStatus(LotStatus.RUNNING),HttpStatus.FOUND);
    }

    @Override
    @HystrixCommand(groupKey = "bidding", commandKey = "closeBidding",fallbackMethod = "biddingServiceFallback")
    public ResponseEntity<?> getLotbyId(Long lotId) {
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new InvalidIdException("Invalid Lot Id, Check again"));
        try{
            logger.error("Attempting to get Lot by LotId "+" "+LocalDateTime.now());
            BidCollection bidCollection = biddingFeignClient.getBidCollection(lotId);
            System.out.println(bidCollection);
            CombinedLotDetails combinedLotDetails = CombinedLotDetails.builder()
                    .lotId(lot.getLotId())
                    .vin(lot.getVin())
                    .carName(lot.getCarName())
                    .bodyType(lot.getBodyType())
                    .transmissionType(lot.getTransmissionType())
                    .modelYear(lot.getModelYear())
                    .photoUri(lot.getPhotoUri())
                    .listerComment(lot.getListerComment())
                    .startingValue(lot.getStartingValue())
                    .lotStatus(lot.getLotStatus())
                    .startTimestamp(lot.getStartTimestamp())
                    .endDate(lot.getEndDate())
                    .userId(lot.getUserId())
                    .username(lot.getUsername())
                    .currentHighestBid(bidCollection.getCurrentHighestBid())
                    .highestBidUserId(bidCollection.getHighestBidUserId())
                    .highestBidUsername(bidCollection.getHighestBidUsername())
                    .noOfBids(bidCollection.getNoOfBids())
                    .build();
            return new ResponseEntity<>(combinedLotDetails,HttpStatus.OK);
        }
        catch (Exception ex){
            logger.error("Error while buidling combined Lot object "+" "+LocalDateTime.now());
            throw new InvalidIdException(ex.getMessage());
        }
    }



    @Override
    @HystrixCommand(groupKey = "bidding", commandKey = "closeBidding",fallbackMethod = "biddingServiceFallback")
    public ResponseEntity<?> closeLotPremature(Long lotId,Long userId) {
        logger.info("Attempting to close lot by lotId "+" "+lotId+" "+LocalDateTime.now());
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new InvalidIdException("Lot with ID " + lotId + " not found"));

    if(userId != lot.getUserId() ){
        logger.error("Invalid Credentials Entered. "+" "+LocalDateTime.now());
        throw new InvalidUserException("Invalid User, Cannot perform action");
    }
        lot.setLotStatus(LotStatus.CLOSED);
        lotRepository.save(lot);
        biddingFeignClient.closeBiddingService(lotId);
        logger.info("successfully closed lot by lotId "+" "+lotId+" "+LocalDateTime.now());
        return new ResponseEntity<>("successfully closed lot",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getClosedListings() {
        logger.info("Attempting to get closed Lots "+" "+LocalDateTime.now());
        return new ResponseEntity<>(lotRepository.findByLotStatus(LotStatus.CLOSED),HttpStatus.FOUND);
    }


    public Boolean checkValidYear(String modelYear){
        if(modelYear.isEmpty())
            return false;
        int currentYear = LocalDate.now().getYear();
        return Integer.parseInt(modelYear) <= currentYear;
    }

    @HystrixCommand(groupKey = "auth", commandKey = "username",fallbackMethod = "authServiceFallback")
    public String getUsername(Long userId){
        String username = authFeignClient.getUsername(userId);
        if(username.isEmpty())
        {
            throw new InvalidDataException("Invalid UserId");
        }
        return username;
    }

    @Override
    public ResponseEntity<?> checkLotId(Long lotId) {
        return new ResponseEntity<>(lotRepository.existsById(lotId), HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<?> checkLotStatus(Long lotId) {
        LotStatus status =lotRepository.findById(lotId).get().getLotStatus();

        if(status.equals(LotStatus.CLOSED))
            return new ResponseEntity<>(false,HttpStatus.OK);

        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @HystrixCommand(groupKey = "auth", commandKey = "userId",fallbackMethod = "authServiceFallback")
    public Boolean checkUserId(Long userId){

        ResponseEntity<UserIdCheck> response = authFeignClient.checkUserId(userId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().getCheck();
        } else {
            throw new InvalidDataException("Invalid UserId");
        }

    }

    public ResponseEntity<?> authServiceFallback(){
        FallbackResponse fallbackResponse = FallbackResponse.builder()
                .message("Authentication Service is down, Try again later.")
                .timeStamp(LocalDateTime.now())
                .httpCode(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
        return new ResponseEntity<>(fallbackResponse,HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<?> biddingServiceFallback(){
        FallbackResponse fallbackResponse = FallbackResponse.builder()
                .message("Bidding Service is down, Try again later.")
                .timeStamp(LocalDateTime.now())
                .httpCode(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
        return new ResponseEntity<>(fallbackResponse,HttpStatus.SERVICE_UNAVAILABLE);
    }

}
