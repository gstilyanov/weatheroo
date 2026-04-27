package com.weatheroo.weatheroo.controller;

import com.weatheroo.weatheroo.service.WeatherService;
import com.weatheroo.weatheroo.util.Continent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void shouldReturnAverages() throws Exception {
        // Given
        Continent continent = Continent.EUROPE;
        when(weatherService.getAverageTempC(eq(continent), anyInt())).thenReturn(15.5);
        when(weatherService.getAverageTempF(eq(continent), anyInt())).thenReturn(59.9);
        when(weatherService.getSourceCities(eq(continent))).thenReturn(List.of("London", "Paris"));

        // When & Then
        mockMvc.perform(get("/api/v1/weather/averages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.continent == 'Europe')].avgTempC").value(15.5))
                .andExpect(jsonPath("$[?(@.continent == 'Europe')].avgTempF").value(59.9))
                .andExpect(jsonPath("$[?(@.continent == 'Europe')].sourceCities").isArray())
                .andExpect(jsonPath("$[?(@.continent == 'Europe')].period").value("Last 6 Months"));
    }
}
