package de.digitalservice.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VerspaeteterFlug {
    START_AIRPORT_FIRST_ZWISCHENSTOPP("startAirportFirstZwischenstopp"),
    FIRST_ZWISCHENSTOPP_END_AIRPORT("firstZwischenstoppEndAirport"),
    FIRST_AIRPORT_SECOND_ZWISCHENSTOPP("firstAirportSecondZwischenstopp"),
    SECOND_ZWISCHENSTOPP_END_AIRPORT("secondZwischenstoppEndAirport"),
    SECOND_AIRPORT_THIRD_ZWISCHENSTOPP("secondAirportThirdZwischenstopp"),
    THIRD_ZWISCHENSTOPP_END_AIRPORT("thirdZwischenstoppEndAirport");

    private final String value;

    VerspaeteterFlug(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static VerspaeteterFlug fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (VerspaeteterFlug verspaeteterFlug : VerspaeteterFlug.values()) {
            if (verspaeteterFlug.value.equalsIgnoreCase(value)) {
                return verspaeteterFlug;
            }
        }
        throw new IllegalArgumentException("Unknown VerspaeteterFlug: " + value);
    }
}
