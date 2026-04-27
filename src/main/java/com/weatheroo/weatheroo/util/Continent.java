package com.weatheroo.weatheroo.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Continents supported by the Weatheroo App")
public enum Continent {
    AFRICA("Africa"),
    ASIA("Asia"),
    EUROPE("Europe"),
    NORTH_AMERICA("North America"),
    SOUTH_AMERICA("South America"),
    OCEANIA("Oceania");

    private final String displayName;

    Continent(String displayName) {
        this.displayName = displayName;
    }

}
