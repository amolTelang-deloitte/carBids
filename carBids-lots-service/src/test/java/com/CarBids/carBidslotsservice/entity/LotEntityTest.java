package com.CarBids.carBidslotsservice.entity;

import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import org.junit.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class LotEntityTest{

    @Test
    public void testLotCreation() {
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 21, 12, 0, 0);
        Date date = new Date();
        List<String> photosURI = new ArrayList<>();
        Lot lotTest = new Lot();
        lotTest.setVin("1234567890");
        lotTest.setCarName("Audi A4");
        lotTest.setBodyType(BodyType.COUPE);
        lotTest.setTransmissionType(TransmissionType.AUTOMATIC);
        lotTest.setModelYear("2023");
        lotTest.setPhotoUri(photosURI);
        lotTest.setListerComment("This is a great car!");
        lotTest.setStartingValue("50000");
        lotTest.setLotStatus(LotStatus.RUNNING);
        lotTest.setStartTimestamp(fixedDateTime);
        lotTest.setEndDate(date);
        lotTest.setUserId(1L);
        lotTest.setUsername("johndoe");

        assertThat(lotTest).isNotNull();
        assertThat(lotTest.getVin()).isEqualTo("1234567890");
        assertThat(lotTest.getCarName()).isEqualTo("Audi A4");
        assertThat(lotTest.getBodyType()).isEqualTo(BodyType.COUPE);
        assertThat(lotTest.getTransmissionType()).isEqualTo(TransmissionType.AUTOMATIC);
        assertThat(lotTest.getModelYear()).isEqualTo("2023");
        assertThat(lotTest.getListerComment()).isEqualTo("This is a great car!");
        assertThat(lotTest.getStartingValue()).isEqualTo("50000");
        assertThat(lotTest.getLotStatus()).isEqualTo(LotStatus.RUNNING);
        assertThat(lotTest.getStartTimestamp()).isEqualTo(fixedDateTime);
        assertThat(lotTest.getEndDate()).isEqualTo(date);
        assertThat(lotTest.getUserId()).isEqualTo(1L);
        assertThat(lotTest.getUsername()).isEqualTo("johndoe");
    }
}
