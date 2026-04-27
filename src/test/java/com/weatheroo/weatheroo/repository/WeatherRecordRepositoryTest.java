package com.weatheroo.weatheroo.repository;

import com.weatheroo.weatheroo.entity.WeatherRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WeatherRecordRepositoryTest {

    @Autowired
    private WeatherRecordRepository weatherRecordRepository;

    @Test
    void shouldSaveAndFindWeatherRecord() {
        // Given
        WeatherRecord record = WeatherRecord.builder()
                .cityName("London")
                .continent("Europe")
                .recordDate(LocalDate.now())
                .avgTempC(15.0)
                .avgTempF(59.0)
                .build();

        weatherRecordRepository.save(record);

        // When
        List<WeatherRecord> records = weatherRecordRepository.findAllByContinentAndRecordDateBetween(
                "Europe", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        // Then
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getCityName()).isEqualTo("London");
    }

    @Test
    void shouldCheckIfExistsByCityNameAndRecordDate() {
        // Given
        LocalDate date = LocalDate.of(2023, 10, 27);
        WeatherRecord record = WeatherRecord.builder()
                .cityName("Paris")
                .continent("Europe")
                .recordDate(date)
                .avgTempC(12.0)
                .avgTempF(53.6)
                .build();

        weatherRecordRepository.save(record);

        // When & Then
        assertThat(weatherRecordRepository.existsByCityNameAndRecordDate("Paris", date)).isTrue();
        assertThat(weatherRecordRepository.existsByCityNameAndRecordDate("Paris", date.plusDays(1))).isFalse();
        assertThat(weatherRecordRepository.existsByCityNameAndRecordDate("London", date)).isFalse();
    }
}
