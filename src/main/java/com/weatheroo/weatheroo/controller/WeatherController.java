package com.weatheroo.weatheroo.controller;

import com.weatheroo.weatheroo.dto.WeatherAverageResponse;
import com.weatheroo.weatheroo.service.WeatherService;
import com.weatheroo.weatheroo.util.Continent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Weather API", description = "Endpoints for historical weather data management and visualization")
@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(summary = "Get continent weather averages", description = "Returns the average temperature for the last 6 months.")
    @GetMapping("/averages")
    public List<WeatherAverageResponse> getAverages(
            @Parameter(description = "Optional continent to filter")
            @RequestParam(required = false) Continent continent) {

        List<Continent> continentsToQuery = (continent != null)
                ? List.of(continent)
                : Arrays.asList(Continent.values());

        return continentsToQuery.stream()
                .map(cont -> new WeatherAverageResponse(
                        cont.getDisplayName(),
                        weatherService.getAverageTempC(cont, 6),
                        weatherService.getAverageTempF(cont, 6),
                        weatherService.getSourceCities(cont),
                        "Last 6 Months"
                ))
                .toList();
    }

    @Operation(summary = "Trigger manual weather sync", description = "Asynchronously syncs missing weather data for the last 6 months.")
    @PostMapping("/sync")
    public ResponseEntity<String> triggerSync(
            @Parameter(description = "Optional continent to filter sync")
            @RequestParam(required = false) Continent continent) {

        weatherService.syncMissingData(continent);

        return ResponseEntity.accepted()
                .body("Weather synchronization triggered successfully " +
                        (continent != null ? "for " + continent.getDisplayName() : "for all continents") +
                        ". It will run in the background.");
    }
}