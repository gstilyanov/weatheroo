package com.weatheroo.weatheroo.repository;

import com.weatheroo.weatheroo.entity.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Repository for managing {@link WeatherRecord} entities.
 */
@Repository
public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {

    List<WeatherRecord> findAllByContinentAndRecordDateBetween(String continent, LocalDate startDate, LocalDate endDate);
    
    boolean existsByCityNameAndRecordDate(String cityName, LocalDate recordDate);

    @Query("SELECT w.recordDate FROM WeatherRecord w WHERE w.cityName = :cityName AND w.recordDate >= :startDate AND w.recordDate <= :endDate")
    Set<LocalDate> findExistingDatesForCityInDateRange(
            @Param("cityName") String cityName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
