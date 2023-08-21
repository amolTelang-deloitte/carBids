package com.CarBids.carBidslotsservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDetails {
    private String vin;
    private String carName;
    private String bodyType;
    private String transmissionType;
    private String modelYear;
    private List<String> carPhotosURI = new ArrayList<>();
    private String listerComment;
    private String startingValue;
    private Date endDate;

}
