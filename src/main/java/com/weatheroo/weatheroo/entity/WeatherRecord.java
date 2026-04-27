package com.weatheroo.weatheroo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity representing historical weather data for a specific city and date.
 */
@Entity
@Table(
    name = "weather_records",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"city_name", "record_date"})
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Column(nullable = false)
    private String continent;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "avg_temp_c")
    private Double avgTempC;

    @Column(name = "avg_temp_f")
    private Double avgTempF;
}
