package de.digitalservice.service.claims;

import static de.digitalservice.codes.CodeUtils.createCodeFromValue;

import java.math.BigInteger;

import de.xjustiz.CodeKLAVERAnspruchsartTyp3;
import de.xjustiz.TypeGDSRefRollennummer;
import de.xjustiz.TypeKLAVERAnspruch;

public class AnspruchBuilder {

    private static final String CODE_ANSPRUCH_PATH = "codes/KLAVER.Anspruchsart_3.6.xml";

    private final TypeKLAVERAnspruch anspruch = new TypeKLAVERAnspruch();

    public AnspruchBuilder fortlaufendeNummer(BigInteger nummer) {
        anspruch.setFortlaufendeNummer(nummer);
        return this;
    }

    public AnspruchBuilder anspruchsart(String value) {
        anspruch.setAnspruchsart(
                createCodeFromValue(
                        CodeKLAVERAnspruchsartTyp3.class,
                        value,
                        CODE_ANSPRUCH_PATH));
        return this;
    }

    public AnspruchBuilder anspruchsgegenstand(String gegenstand) {
        anspruch.setAnspruchsgegenstand(gegenstand);
        return this;
    }

    public AnspruchBuilder anspruchssteller(int rollennummer) {
        anspruch.getAnspruchssteller().add(refRollennummer(rollennummer));
        return this;
    }

    public AnspruchBuilder anspruchsgegner(int rollennummer) {
        anspruch.getAnspruchsgegner().add(refRollennummer(rollennummer));
        return this;
    }

    public TypeKLAVERAnspruch build() {
        return anspruch;
    }

    private TypeGDSRefRollennummer refRollennummer(int rollennummer) {
        var ref = new TypeGDSRefRollennummer();
        ref.setRefRollennummer(String.valueOf(rollennummer));
        return ref;
    }
}
