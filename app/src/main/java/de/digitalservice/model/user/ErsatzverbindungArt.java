package de.digitalservice.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ErsatzverbindungArt {

    FLIGHT("flug"),
    OTHER("etwasAnderes"),
    NO_ARRIVAL("keineAnkunft");

    private final String value;

    ErsatzverbindungArt(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ErsatzverbindungArt fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (ErsatzverbindungArt art : ErsatzverbindungArt.values()) {
            if (art.value.equalsIgnoreCase(value)) {
                return art;
            }
        }
        throw new IllegalArgumentException("Unknown ErsatzverbindungArt: " + value);
    }
}