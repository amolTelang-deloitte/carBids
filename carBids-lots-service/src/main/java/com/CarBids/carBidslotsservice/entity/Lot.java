package com.CarBids.carBidslotsservice.entity;

import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import com.CarBids.carBidslotsservice.service.LotService;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="lot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;
    @NonNull
    private String vin;
    @NonNull
    private String carName;
    @NonNull
    private BodyType bodyType;
    @NonNull
    private TransmissionType transmissionType;
    @NonNull
    private String modelYear;

    @NonNull
    @ElementCollection
    @CollectionTable(name = "car_photo_uri") // Name of the table to store tags
    @Column(name = "uri")
    private List<String> photoUri;
    @NonNull
    private String listerComment;
    @NonNull
    private String startingValue;
    @NonNull
    private String minBidValue;
    @NonNull
    private LotStatus lotStatus;
    @NonNull
    private LocalDateTime startTimestamp;
    @NonNull
    private Date endDate;
    @NonNull
    private Long userId;
}
