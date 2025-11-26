package de.digitalservice.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Bereich {

    DELAYED("verspaetet"),
    CANCELLED("annullierung"),
    DENIED_BOARDING("nichtbefoerderung");

    private final String value;

    Bereich(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Bereich fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (Bereich bereich : Bereich.values()) {
            if (bereich.value.equalsIgnoreCase(value)) {
                return bereich;
            }
        }
        throw new IllegalArgumentException("Unknown Bereich: " + value);
    }

}
