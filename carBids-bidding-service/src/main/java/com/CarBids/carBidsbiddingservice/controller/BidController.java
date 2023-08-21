package com.CarBids.carBidsbiddingservice.controller;

import com.CarBids.carBidsbiddingservice.dto.BidDetails;
import com.CarBids.carBidsbiddingservice.dto.CollectionDTO;
import com.CarBids.carBidsbiddingservice.dto.CollectionDetails;
import com.CarBids.carBidsbiddingservice.service.IBidService;
import com.CarBids.carBidsbiddingservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bid")
public class BidController {
    private static final Logger logger = LoggerFactory.getLogger(BidController.class);
    private final IBidService bidService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BidController(IBidService bidService, JwtUtil jwtUtil) {
        this.bidService = bidService;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/create/lotCollection")
    public ResponseEntity<?>createLotCollection(@RequestBody CollectionDetails collectionDetails){
        logger.info("Attempting to create lot Collection"+" "+ LocalDateTime.now());
        return bidService.saveBidCollection(collectionDetails);
    }

    @PostMapping("/create/bid")
    public ResponseEntity<?>addBid(@RequestBody BidDetails bidDetails,HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Attempting to add Bid"+" "+bidDetails+" "+ LocalDateTime.now());
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            return bidService.addBid(bidDetails,userId);
        }
        else{
            logger.error("Missing authorization header"+" "+ LocalDateTime.now());
            throw new RuntimeException("missing authorization header");
        }
    }

    @GetMapping("/get/collection")
    public CollectionDTO getCollection(@RequestParam(required = true)Long lotId){
        logger.info("Attempting to get Lot Collection"+" "+lotId+" "+ LocalDateTime.now());
        return bidService.getCollection(lotId);
    }

    @PutMapping("/close")
    public ResponseEntity<?>closeBidding(@RequestParam(required = true)Long collectionId){
        logger.info("Attempting to close Lot Collection"+" "+collectionId+" "+ LocalDateTime.now());
        return bidService.closeBidding(collectionId);
    }

}
