package com.CarBids.carBidslotsservice.repository;

import com.CarBids.carBidslotsservice.entity.Lot;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long>, JpaSpecificationExecutor<Lot> {
    List<Lot> findByLotStatus(LotStatus lotStatus);
}
