package com.CarBids.carBidslotsservice.repository;

import com.CarBids.carBidslotsservice.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot, Long> {
}
