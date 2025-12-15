package de.digitalservice.model.fgrUser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Videoverhandlung {

    YES("yes"),
    NO("no"),
    NO_SPECIFICATION("noSpecification");

    private final String value;

    Videoverhandlung(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Videoverhandlung fromValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        for (Videoverhandlung videoverhandlung : Videoverhandlung.values()) {
            if (videoverhandlung.value.equalsIgnoreCase(value)) {
                return videoverhandlung;
            }
        }
        throw new IllegalArgumentException("Unknown Videoverhandlung: " + value);
    }

}
