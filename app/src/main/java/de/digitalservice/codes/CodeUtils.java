package de.digitalservice.codes;

import java.util.List;

import de.xoev.schemata.code._1_0.Code;

public class CodeUtils {

    /**
     * Generic method to create any JAXB Code object from its human-readable value.
     *
     * @param codeClass          Class of the JAXB Code object (e.g.,
     *                           CodeGDSGerichteTyp3.class)
     * @param humanReadableValue The text value to look up in the codelist
     * @param resourcePath       Path to the Genericode XML/JSON in resources
     * @return instance of the JAXB Code object with the correct code set
     */
    public static <T extends Code> T createCodeFromValue(
            Class<T> codeClass,
            String humanReadableValue,
            String resourcePath) {
        try {
            // Load codelist
            List<CodeListEntry> entries = CodeListLoader.loadFromXml(resourcePath);

            // Find the code
            String code = entries.stream()
                    .filter(e -> e.value().equals(humanReadableValue))
                    .map(CodeListEntry::code)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Value '" + humanReadableValue + "' not found in codelist " + resourcePath));

            // Create JAXB Code object
            return CodeFactory.create(codeClass, code);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Code object for " + humanReadableValue, e);
        }
    }
}
