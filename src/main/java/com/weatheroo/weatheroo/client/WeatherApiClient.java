package com.weatheroo.weatheroo.client;

import com.weatheroo.weatheroo.dto.external.WeatherHistoryResponse;
import com.weatheroo.weatheroo.entity.WeatherRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherApiClient {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public List<WeatherRecord> fetchHistoryRange(String city, String continent, LocalDate startDate, LocalDate endDate) {
        String url = UriComponentsBuilder.fromUriString(apiUrl)
                .path("/history.json")
                .queryParam("key", apiKey)
                .queryParam("q", city)
                .queryParam("dt", startDate.toString())
                .queryParam("end_dt", endDate.toString())
                .toUriString();

        try {
            WeatherHistoryResponse response = restTemplate.getForObject(url, WeatherHistoryResponse.class);
            if (response != null && response.forecast() != null) {
                return response.forecast().forecastday().stream()
                        .map(fd -> WeatherRecord.builder()
                                .cityName(city)
                                .continent(continent)
                                .recordDate(LocalDate.parse(fd.date()))
                                .avgTempC(fd.day().avgTempC())
                                .avgTempF(fd.day().avgTempF())
                                .build()).toList();
            }
        } catch (Exception e) {
            log.error("Error fetching weather range for {} from {} to {}: {}", city, startDate, endDate, e.getMessage());
        }
        return List.of();
    }
}
