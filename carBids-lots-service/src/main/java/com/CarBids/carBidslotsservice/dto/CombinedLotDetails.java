package com.CarBids.carBidslotsservice.dto;

import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CombinedLotDetails {
    private Long lotId;
    private String vin;
    private String carName;
    private BodyType bodyType;
    private TransmissionType transmissionType;
    private String modelYear;
    private List<String> photoUri;
    private String listerComment;
    private String startingValue;
    private LotStatus lotStatus;
    private LocalDateTime startTimestamp;
    private Date endDate;
    private Long userId;
    private String username;
    private String currentHighestBid;
    private Long highestBidUserId;
    private String highestBidUsername;
    private Integer noOfBids;
}
