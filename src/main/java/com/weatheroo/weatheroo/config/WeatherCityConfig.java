package com.weatheroo.weatheroo.config;

import com.weatheroo.weatheroo.util.Continent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "weather")
public class WeatherCityConfig {
    private Map<Continent, List<String>> cities;
    private long syncDelay;
}