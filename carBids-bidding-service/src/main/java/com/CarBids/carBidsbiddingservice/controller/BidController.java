package com.CarBids.carBidsbiddingservice.controller;

import com.CarBids.carBidsbiddingservice.dto.BidDetails;
import com.CarBids.carBidsbiddingservice.dto.CollectionDetails;
import com.CarBids.carBidsbiddingservice.service.IBidService;
import com.CarBids.carBidsbiddingservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bid")
public class BidController {
    private final IBidService bidService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BidController(IBidService bidService, JwtUtil jwtUtil) {
        this.bidService = bidService;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/create/lotCollection")
    public ResponseEntity<?>createLotCollection(@RequestBody CollectionDetails collectionDetails){
        return bidService.saveBidCollection(collectionDetails);
    }

    @PostMapping("/create/bid")
    public ResponseEntity<?>addBid(@RequestBody BidDetails bidDetails,HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            return bidService.addBid(bidDetails,userId);
        }
        else{
            throw new RuntimeException("missing authorization header");
        }
    }

    @PutMapping("/close")
    public ResponseEntity<?>closeBidding(@RequestParam(required = true)Long collectionId){
        return bidService.closeBidding(collectionId);
    }

}
