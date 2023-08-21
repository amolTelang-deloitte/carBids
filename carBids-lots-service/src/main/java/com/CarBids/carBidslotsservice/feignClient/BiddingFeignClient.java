package com.CarBids.carBidslotsservice.feignClient;

import com.CarBids.carBidslotsservice.dto.BidCollection;
import com.CarBids.carBidslotsservice.dto.CollectionDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ComponentScan
@FeignClient(name = "CARBIDS-BIDDING-SERVICE",url = "http://localhost:8096/api/bid")
public interface BiddingFeignClient {

    @PostMapping("/create/lotCollection")
    public ResponseEntity<?>startBiddingService(@RequestBody CollectionDetails collectionDetails);

    @GetMapping("/get/collection")
    BidCollection getBidCollection(@RequestParam(required = true)Long lotId);
    @PutMapping("/close")
    void closeBiddingService(@RequestParam(required = true)Long collectionId);

}
