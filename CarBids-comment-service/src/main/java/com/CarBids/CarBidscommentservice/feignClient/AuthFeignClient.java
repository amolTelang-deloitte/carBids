package com.CarBids.CarBidscommentservice.feignClient;

import com.CarBids.CarBidscommentservice.dto.UserIdCheck;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ComponentScan
@FeignClient(name = "CARBIDS-AUTHENTICATION-SERVICE", url = "http://localhost:8090/auth")
public interface AuthFeignClient {

    @GetMapping("/get/username")
    String getUsername(@RequestParam(required = true) Long userId);

    @GetMapping("/check/userId")
    ResponseEntity<UserIdCheck> checkUserId(@RequestParam(required = true)Long userId);




}
