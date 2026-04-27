package com.weatheroo.weatheroo.service;

import com.weatheroo.weatheroo.config.WeatherCityConfig;
import com.weatheroo.weatheroo.entity.WeatherRecord;
import com.weatheroo.weatheroo.repository.WeatherRecordRepository;
import com.weatheroo.weatheroo.util.Continent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRecordRepository weatherRecordRepository;
    private final WeatherSyncManager syncManager;
    private final WeatherCityConfig weatherCityConfig;

    /**
     * Triggers the background synchronization process.
     */
    @Async
    public void syncMissingData(Continent filterContinent) {
        log.info("Starting sync. Filter: {}", filterContinent != null ? filterContinent : "All");

        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusMonths(6);

        Map<Continent, List<String>> citiesMap = weatherCityConfig.getCities();

        if (citiesMap == null) {
            log.error("Weather cities configuration is missing!");
            return;
        }

        int citiesProcessed = 0;
        for (Map.Entry<Continent, List<String>> entry : citiesMap.entrySet()) {
            Continent continent = entry.getKey();

            if (filterContinent != null && filterContinent != continent) {
                continue;
            }

            for (String city : entry.getValue()) {
                runCitySync(city, continent.getDisplayName(), startDate, endDate);
                citiesProcessed++;
            }
        }
        log.info("Synchronization process finished. Processed {} cities. Data is now up to date.", citiesProcessed);
    }

    private void runCitySync(String city, String continentName, LocalDate start, LocalDate end) {
        LocalDate currentStart = start;
        while (currentStart.isBefore(end)) {
            LocalDate currentEnd = currentStart.plusDays(29);
            if (currentEnd.isAfter(end)) {
                currentEnd = end;
            }

            syncManager.processChunk(city, continentName, currentStart, currentEnd);

            currentStart = currentEnd.plusDays(1);
        }
    }


    public Double getAverageTempC(Continent continent, int months) {
        return calculateAverage(continent, months, WeatherRecord::getAvgTempC);
    }

    public Double getAverageTempF(Continent continent, int months) {
        return calculateAverage(continent, months, WeatherRecord::getAvgTempF);
    }

    private Double calculateAverage(Continent continent, int months, ToDoubleFunction<WeatherRecord> extractor) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(months);

        List<WeatherRecord> records = weatherRecordRepository.findAllByContinentAndRecordDateBetween(
                continent.getDisplayName(), start, end);

        return records.stream()
                .mapToDouble(extractor)
                .average()
                .orElse(0.0);
    }

    public List<String> getSourceCities(Continent continent) {
        if (weatherCityConfig.getCities() == null) {
            return List.of();
        }
        return weatherCityConfig.getCities().getOrDefault(continent, List.of());
    }
}