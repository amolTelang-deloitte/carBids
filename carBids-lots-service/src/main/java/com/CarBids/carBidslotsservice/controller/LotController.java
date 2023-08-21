package com.CarBids.carBidslotsservice.controller;

import com.CarBids.carBidslotsservice.dto.CarDetails;
import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import com.CarBids.carBidslotsservice.exception.exceptions.InvalidAuthException;
import com.CarBids.carBidslotsservice.service.ILotService;
import com.CarBids.carBidslotsservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/lot")
public class LotController {

    private static final Logger logger = LoggerFactory.getLogger(LotController.class);
    private final ILotService lotService;
    private final JwtUtil jwtUtil;

    @Autowired
    public LotController(ILotService lotService, JwtUtil jwtUtil) {
        this.lotService = lotService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createLot(@RequestBody CarDetails carDetails, HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Attempting to create lot");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            logger.info("User successfully verified");
            return lotService.saveLot(carDetails,userId);
        }
        else{
            logger.error("Invalid JWT Token");
            throw new InvalidAuthException("Invalid Credentials");
        }
    }

    //filter listing by model year, Transmission Type, Body Style
    @GetMapping("/filter")
    public ResponseEntity<?> filterBasedOnProperty(
            @RequestParam(required = false)String modelYear,
            @RequestParam(required = false)String bodyType,
            @RequestParam(required = false)String transmissionType
            ){
            return lotService.getFilteredLot(modelYear,bodyType,transmissionType);
    }

    //get all listing
    @GetMapping("/get/all")
    public ResponseEntity<?> getAllLot(){
        return lotService.getAllLot();
    }

    //get all active listing
    @GetMapping("/get/active")
    public ResponseEntity<?> getActiveListings(){
        return lotService.getActiveListings();
    }

    @GetMapping("/get/closed")
    public ResponseEntity<?> getClosedListings(){
        return lotService.getClosedListings();
    }

    @GetMapping("/get/getLot")
    public ResponseEntity<?> getLotById(@RequestParam(required = true)Long lotId){
        return lotService.getLotbyId(lotId);
    }

    @GetMapping("/check/lotId")
    public ResponseEntity<?> checkLotId(@RequestParam(required = true)Long lotId){
        return lotService.checkLotId(lotId);
    }

    @GetMapping("/check/lotStatus")
    public ResponseEntity<?> checkLotStatus(@RequestParam(required = true)Long lotId){
        return lotService.checkLotStatus(lotId);
    }

    @PostMapping("/end/premature")
    public ResponseEntity<?> clostLotPremature(@RequestParam(required = true)Long lotId,HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Attempting to create lot");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            logger.info("User successfully verified");
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            return lotService.closeLotPremature(lotId,userId);
        }
        else{
            logger.error("Invalid JWT Token");
            throw new InvalidAuthException("Invalid Credentials");
        }
    }
}
