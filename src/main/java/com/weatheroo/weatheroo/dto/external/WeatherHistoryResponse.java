package com.weatheroo.weatheroo.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record WeatherHistoryResponse(Forecast forecast) {
    public record Forecast(List<ForecastDay> forecastday) {}
    public record ForecastDay(String date, Day day) {}
    public record Day(
        @JsonProperty("avgtemp_c") Double avgTempC,
        @JsonProperty("avgtemp_f") Double avgTempF
    ) {}
}
