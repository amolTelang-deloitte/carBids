package com.CarBids.carBidslotsservice.controller;

import com.CarBids.carBidslotsservice.dto.CarDetails;
import com.CarBids.carBidslotsservice.service.ILotService;
import com.CarBids.carBidslotsservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/lot")
public class LotController {

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
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            return lotService.saveLot(carDetails,userId);
        }
        else{
            throw new RuntimeException("missing authorization header");
        }
    }

}
