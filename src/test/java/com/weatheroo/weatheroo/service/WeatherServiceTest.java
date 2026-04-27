package com.weatheroo.weatheroo.service;

import com.weatheroo.weatheroo.config.WeatherCityConfig;
import com.weatheroo.weatheroo.entity.WeatherRecord;
import com.weatheroo.weatheroo.repository.WeatherRecordRepository;
import com.weatheroo.weatheroo.util.Continent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherRecordRepository weatherRecordRepository;

    @Mock
    private WeatherSyncManager weatherSyncManager;

    @Mock
    private WeatherCityConfig weatherCityConfig;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void shouldSyncMissingData() {
        when(weatherCityConfig.getCities()).thenReturn(Map.of(
                Continent.EUROPE, List.of("London"),
                Continent.ASIA, List.of("Tokyo")
        ));

        // When
        weatherService.syncMissingData(null);

        // Then:
        verify(weatherSyncManager, atLeastOnce())
                .processChunk(eq("London"), anyString(), any(), any());

        verify(weatherSyncManager, atLeastOnce())
                .processChunk(eq("Tokyo"), anyString(), any(), any());

        verify(weatherSyncManager, times(14)).processChunk(anyString(), anyString(), any(), any());
    }

    @Test
    void shouldCalculateAverages() {
        Continent continent = Continent.EUROPE;
        List<WeatherRecord> records = List.of(
                WeatherRecord.builder().avgTempC(10.0).avgTempF(50.0).build(),
                WeatherRecord.builder().avgTempC(20.0).avgTempF(68.0).build()
        );

        when(weatherRecordRepository.findAllByContinentAndRecordDateBetween(
                eq(continent.getDisplayName()), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(records);

        // When
        Double avgC = weatherService.getAverageTempC(continent, 6);
        Double avgF = weatherService.getAverageTempF(continent, 6);

        // Then
        assertThat(avgC).isEqualTo(15.0);
        assertThat(avgF).isEqualTo(59.0);
    }
}