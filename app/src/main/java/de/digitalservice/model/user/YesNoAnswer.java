package de.digitalservice.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum YesNoAnswer {
    YES("yes"),
    NO("no");

    private final String value;

    YesNoAnswer(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static YesNoAnswer fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (YesNoAnswer answer : YesNoAnswer.values()) {
            if (answer.value.equalsIgnoreCase(value)) {
                return answer;
            }
        }
        throw new IllegalArgumentException("Unknown YesNoAnswer: " + value);
    }
}