package com.weatheroo.weatheroo.service;

import com.weatheroo.weatheroo.client.WeatherApiClient;
import com.weatheroo.weatheroo.config.WeatherCityConfig;
import com.weatheroo.weatheroo.entity.WeatherRecord;
import com.weatheroo.weatheroo.repository.WeatherRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherSyncManager {

    private final WeatherRecordRepository weatherRecordRepository;
    private final WeatherApiClient weatherApiClient;
    private final WeatherCityConfig weatherCityConfig;

    @Transactional
    public void processChunk(String city, String continentName, LocalDate start, LocalDate end) {
        if (weatherRecordRepository.existsByCityNameAndRecordDate(city, end)) {
            return;
        }

        List<WeatherRecord> fetched = weatherApiClient.fetchHistoryRange(city, continentName, start, end);
        Set<LocalDate> existingDates = weatherRecordRepository.findExistingDatesForCityInDateRange(city, start, end);

        List<WeatherRecord> toSave = fetched.stream()
                .filter(r -> !existingDates.contains(r.getRecordDate()))
                .toList();

        if (!toSave.isEmpty()) {
            weatherRecordRepository.saveAll(toSave);
        }

        try {
            long delay = weatherCityConfig.getSyncDelay();
            if (delay > 0) {
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
