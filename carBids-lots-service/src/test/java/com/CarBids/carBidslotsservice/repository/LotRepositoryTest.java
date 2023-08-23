package com.CarBids.carBidslotsservice.repository;

import com.CarBids.carBidslotsservice.entity.Lot;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import com.CarBids.carBidslotsservice.service.LotService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LotRepositoryTest {
    @Mock
    private LotRepository lotRepository;

    @InjectMocks
    private LotService lotService;

    @Test
    public void testFindByLotStatus() {

        LotStatus lotStatus = LotStatus.RUNNING;
        List<Lot> expectedLotTests = Arrays.asList(new Lot(), new Lot());
        when(lotRepository.findByLotStatus(lotStatus)).thenReturn(expectedLotTests);

        List<Lot> result = lotRepository.findByLotStatus(lotStatus);

        assertThat(result).isEqualTo(expectedLotTests);
        verify(lotRepository).findByLotStatus(lotStatus);
    }

    @Test
    public void testFindByEndDateBeforeAndLotStatus() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LotStatus lotStatus = LotStatus.RUNNING;
        List<Lot> expectedLotTests = Arrays.asList(new Lot(), new Lot());

        when(lotRepository.findByEndDateBeforeAndLotStatus(localDateTime, lotStatus)).thenReturn(expectedLotTests);

        List<Lot> result = lotRepository.findByEndDateBeforeAndLotStatus(localDateTime, lotStatus);

        assertThat(result).isEqualTo(expectedLotTests);
        verify(lotRepository).findByEndDateBeforeAndLotStatus(localDateTime, lotStatus);
    }
}
