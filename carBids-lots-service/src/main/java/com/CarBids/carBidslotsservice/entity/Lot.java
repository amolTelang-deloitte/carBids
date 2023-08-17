package com.CarBids.carBidslotsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;
    @NonNull
    private String vin;
    @NonNull
    private String carName;
    @NonNull
    private String bodyType;
    @NonNull
    private String transmissionType;
    @NonNull
    private String modelYear;
    private List<String> carFeatures=new ArrayList<>();
    @NonNull
    private List<String>carPhotosURI = new ArrayList<>();
    private String listerComment;
    @NonNull
    private String startingValue;
    @NonNull
    private String minBidValue;
    @NonNull
    private String lotStatus;
    @NonNull
    private LocalDateTime startTimestamp;
    @NonNull
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
