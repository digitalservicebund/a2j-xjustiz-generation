package de.digitalservice.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Anrede {
    HERR("Herr"),
    FRAU("Frau"),
    KEINE_ANGABE("");

    private final String value;

    Anrede(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Anrede fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (Anrede anrede : Anrede.values()) {
            if (anrede.value.equalsIgnoreCase(value)) {
                return anrede;
            }
        }
        throw new IllegalArgumentException("Unknown Anrede: " + value);
    }
}