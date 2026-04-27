package com.weatheroo.weatheroo.scheduler;

import com.weatheroo.weatheroo.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataScheduler {

    private final WeatherService weatherService;

    /**
     * Daily sync at 2 AM to fetch the previous day's weather for all cities.
     * This keeps the dashboard up-to-date automatically once initial data is present.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void dailySync() {
        log.info("Triggering scheduled daily weather data sync...");
        weatherService.syncMissingData(null);
    }
}
