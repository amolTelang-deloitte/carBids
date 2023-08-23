package com.CarBids.CarBidscommentservice.feignClient;

import com.CarBids.CarBidscommentservice.dto.UserIdCheck;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.ConnectException;

@ComponentScan
@FeignClient(name = "CARBIDS-LOTS-SERVICE", url = "http://localhost:8093/api/lot")
public interface LotFeignClient {
@GetMapping("/check/lotId")
ResponseEntity<UserIdCheck> checkLotId(@RequestParam(required = true)Long lotId);
    @GetMapping("/check/lotStatus")
    ResponseEntity<UserIdCheck> checkLotStatus(@RequestParam(required = true)Long lotId);
}
