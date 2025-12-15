package de.digitalservice.service;

import de.digitalservice.model.fgrUser.UserData;
import de.xjustiz.CodeGDSGerichteTyp3;
import de.xjustiz.TypeGDSHerstellerinformation;
import de.xjustiz.TypeGDSKommunikationspartner;
import de.xjustiz.TypeGDSNachrichtenkopf;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.UUID;

import static de.digitalservice.codes.CodeUtils.createCodeFromValue;

public class NachrichtenkopfGenerator {

    private static final String XJUSTIZ_VERSION = "3.6";
    private static final String COURT_CODELIST_PATH = "codes/GDS.Gerichte_3.6.xml";

    private final TypeGDSNachrichtenkopf nachrichtenkopf;
    private UUID uuid = UUID.randomUUID();

    public NachrichtenkopfGenerator() {
        this.nachrichtenkopf = new TypeGDSNachrichtenkopf();
    }

    public TypeGDSNachrichtenkopf createNachrichtenkopf(UserData userData, String companyName, String productName,
            String manufacturer, String version)
            throws DatatypeConfigurationException {

        nachrichtenkopf.setErstellungszeitpunkt(now());
        nachrichtenkopf.setAbsender(createAbsender(companyName));
        nachrichtenkopf.setEmpfaenger(createEmpfaenger(userData.getCourtName()));
        nachrichtenkopf.setHerstellerinformation(createHerstellerinformation(productName, manufacturer, version));

        nachrichtenkopf.setXjustizVersion(XJUSTIZ_VERSION);

        return nachrichtenkopf;
    }

    private XMLGregorianCalendar now() throws DatatypeConfigurationException {
        GregorianCalendar cal = GregorianCalendar.from(ZonedDateTime.now());
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }

    private TypeGDSNachrichtenkopf.Absender createAbsender(String companyName) {
        var absender = new TypeGDSNachrichtenkopf.Absender();
        var partner = new TypeGDSKommunikationspartner();
        var auswahl = new TypeGDSKommunikationspartner.AuswahlKommunikationspartner();

        auswahl.setSonstige(companyName);
        partner.setAuswahlKommunikationspartner(auswahl);
        absender.setInformationen(partner);
        absender.setEigeneNachrichtenID(uuid.toString());

        return absender;
    }

    private TypeGDSNachrichtenkopf.Empfaenger createEmpfaenger(String courtName) {
        var empfaenger = new TypeGDSNachrichtenkopf.Empfaenger();
        var partner = new TypeGDSKommunikationspartner();
        var auswahl = new TypeGDSKommunikationspartner.AuswahlKommunikationspartner();
        var auswahlAktenzeichen = new TypeGDSNachrichtenkopf.Empfaenger.AuswahlAktenzeichen();
        auswahlAktenzeichen.setAktenzeichenUnbekannt(true);

        empfaenger.setAuswahlAktenzeichen(auswahlAktenzeichen);

        auswahl.setGericht(createCodeFromValue(
                CodeGDSGerichteTyp3.class,
                courtName,
                COURT_CODELIST_PATH));

        partner.setAuswahlKommunikationspartner(auswahl);
        empfaenger.setInformationen(partner);

        return empfaenger;
    }

    private TypeGDSHerstellerinformation createHerstellerinformation(String productName, String manufacturer,
            String version) {
        var herstellerinformation = new TypeGDSHerstellerinformation();
        herstellerinformation.setNameDesProdukts(productName);
        herstellerinformation.setHerstellerDesProdukts(manufacturer);
        herstellerinformation.setVersion(version);
        return herstellerinformation;
    }
}
