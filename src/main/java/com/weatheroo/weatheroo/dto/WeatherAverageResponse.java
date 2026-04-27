package com.weatheroo.weatheroo.dto;

import java.util.List;

/**
 * DTO for representing the average temperature of a continent.
 */
public record WeatherAverageResponse(
    String continent,
    Double avgTempC,
    Double avgTempF,
    List<String> sourceCities,
    String period
) {}
