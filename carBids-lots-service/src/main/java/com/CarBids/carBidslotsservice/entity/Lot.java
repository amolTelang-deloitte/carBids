package com.CarBids.carBidslotsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

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
    private String carName;
    @NonNull
    private String carType;
    @NonNull
    private String carDesc;
    @NonNull
    private String carStatus;
    @NonNull
    private 

}