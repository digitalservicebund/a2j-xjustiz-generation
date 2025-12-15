package de.digitalservice.model.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Title {

    DR("Dr."),
    NONE("");

    private final String value;

    Title(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Title fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (Title title : Title.values()) {
            if (title.value.equalsIgnoreCase(value)) {
                return title;
            }
        }
        throw new IllegalArgumentException("Unknown Title: " + value);
    }

}
