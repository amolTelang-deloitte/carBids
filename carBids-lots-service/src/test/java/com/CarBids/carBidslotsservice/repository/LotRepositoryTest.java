package com.CarBids.carBidslotsservice.repository;

import com.CarBids.carBidslotsservice.entity.Lot;
import com.CarBids.carBidslotsservice.enums.LotEnum.LotStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DataJpaTest
public class LotRepositoryTest {
    @Mock
    private LotRepository lotRepository;

    @InjectMocks
    private LotService lotService;

    @Test
    public void testFindByLotStatus() {
        // Prepare test data
        LotStatus lotStatus = LotStatus.RUNNING
        List<Lot> expectedLots = Arrays.asList(new Lot(), new Lot());

        // Mock repository behavior
        when(lotRepository.findByLotStatus(lotStatus)).thenReturn(expectedLots);

        // Call the service method that uses the repository
        List<Lot> result = lotService.findByLotStatus(lotStatus);

        // Verify the result
        assertThat(result).isEqualTo(expectedLots);
        verify(lotRepository).findByLotStatus(lotStatus);
    }

    @Test
    public void testFindByEndDateBeforeAndLotStatus() {
        // Prepare test data
        LocalDateTime localDateTime = LocalDateTime.now();
        LotStatus lotStatus = LotStatus.RUNNING;
        List<Lot> expectedLots = Arrays.asList(new Lot(), new Lot());

        // Mock repository behavior
        when(lotRepository.findByEndDateBeforeAndLotStatus(localDateTime, lotStatus)).thenReturn(expectedLots);

        // Call the service method that uses the repository
        List<Lot> result = lotService.findByEndDateBeforeAndLotStatus(localDateTime, lotStatus);

        // Verify the result
        assertThat(result).isEqualTo(expectedLots);
        verify(lotRepository).findByEndDateBeforeAndLotStatus(localDateTime, lotStatus);
    }
}
