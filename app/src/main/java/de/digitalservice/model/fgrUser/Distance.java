package de.digitalservice.model.fgrUser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Distance {

    UNDER_1500("entfernungunter1500"),
    MORE_THAN_1500_EU("entfernungmehrals1500inEU"),
    BETWEEN_1500_AND_3500("entfernungzwischen1500und3500"),
    MORE_THAN_3500("entfernungueber3500");

    private final String value;

    Distance(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Distance fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (Distance distance : Distance.values()) {
            if (distance.value.equalsIgnoreCase(value)) {
                return distance;
            }
        }
        throw new IllegalArgumentException("Unknown Distance: " + value);
    }

}