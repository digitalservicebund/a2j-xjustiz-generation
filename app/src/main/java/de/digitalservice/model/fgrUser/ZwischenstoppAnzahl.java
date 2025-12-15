package de.digitalservice.model.fgrUser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ZwischenstoppAnzahl {
    NONE("no"),
    ONE("oneStop"),
    TWO("twoStop"),
    THREE("threeStop");

    private final String value;

    ZwischenstoppAnzahl(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ZwischenstoppAnzahl fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (ZwischenstoppAnzahl zwischenstoppAnzahl : ZwischenstoppAnzahl.values()) {
            if (zwischenstoppAnzahl.value.equalsIgnoreCase(value)) {
                return zwischenstoppAnzahl;
            }
        }
        throw new IllegalArgumentException("Unknown ZwischenstoppAnzahl: " + value);
    }
}