package de.digitalservice.codes;

import de.xoev.schemata.code._1_0.Code;

public class CodeFactory {

    /**
     * Create a Code object of type T with the given code value.
     */
    public static <T extends Code> T create(Class<T> type, String codeValue) {
        try {
            T obj = type.getDeclaredConstructor().newInstance();
            obj.setCode(codeValue);
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create code object: " + type.getName(), e);
        }
    }

}
