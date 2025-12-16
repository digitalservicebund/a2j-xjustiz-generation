package de.digitalservice.service;

import de.xjustiz.CodeGDSRollenbezeichnungTyp3;
import de.xjustiz.CodeGDSStaatenTyp3;
import de.xjustiz.TypeGDSAnschrift;
import de.xjustiz.TypeGDSBeteiligter;
import de.xjustiz.TypeGDSBeteiligung;
import de.xjustiz.TypeGDSGrunddaten;
import de.xjustiz.TypeGDSNameNatuerlichePerson;
import de.xjustiz.TypeGDSNatuerlichePerson;
import de.xjustiz.TypeGDSOrganisation;
import de.xjustiz.TypeGDSBeteiligter.AuswahlBeteiligter;

import static de.digitalservice.codes.CodeUtils.createCodeFromValue;

public class GrunddatenGenerator {

    private static final String ROLE_CODELIST_PATH = "codes/GDS.Rollenbezeichnung_3.6.xml";
    private static final String COUNTRY_CODELIST_PATH = "codes/GDS.Staat_2.4.xml";

    private final TypeGDSGrunddaten grunddaten;

    public GrunddatenGenerator() {
        this.grunddaten = new TypeGDSGrunddaten();
        this.grunddaten.setVerfahrensdaten(new TypeGDSGrunddaten.Verfahrensdaten());
    }

    public TypeGDSGrunddaten getGrunddaten() {
        return grunddaten;
    }

    public TypeGDSGrunddaten addGrunddaten(TypeGDSBeteiligung beteiligung) {

        // TODO: check who sets the following variables (its probably the
        // court)
        // "verfahrensnummer",
        // "instanzdaten",
        // "beteiligung",
        // "terminsdaten"

        this.grunddaten.getVerfahrensdaten().getBeteiligung().add(beteiligung);
        return grunddaten;
    }

    public TypeGDSBeteiligung createBeteiligungForNatuerlichePerson(
            int beteiligtennummer,
            String rollenbezeichnungValue,
            TypeGDSNatuerlichePerson natuerlichePerson) {

        final AuswahlBeteiligter auswahlBeteiligter = new AuswahlBeteiligter();
        auswahlBeteiligter.setNatuerlichePerson(natuerlichePerson);

        return createBeteiligung(beteiligtennummer, rollenbezeichnungValue, auswahlBeteiligter);
    }

    public TypeGDSBeteiligung createBeteiligungForOrganisation(
            int beteiligtennummer,
            String rollenbezeichnungValue,
            TypeGDSOrganisation organisation) {

        final AuswahlBeteiligter auswahlBeteiligter = new AuswahlBeteiligter();
        auswahlBeteiligter.setOrganisation(organisation);

        return createBeteiligung(beteiligtennummer, rollenbezeichnungValue, auswahlBeteiligter);
    }

    private TypeGDSBeteiligung createBeteiligung(
            int beteiligtennummer,
            String rollenbezeichnungValue,
            AuswahlBeteiligter auswahlBeteiligter) {

        final TypeGDSBeteiligung beteiligung = new TypeGDSBeteiligung();

        final TypeGDSBeteiligter beteiligter = new TypeGDSBeteiligter();
        beteiligter.setAuswahlBeteiligter(auswahlBeteiligter);
        beteiligter.setBeteiligtennummer(String.valueOf(beteiligtennummer));

        beteiligung.setBeteiligter(beteiligter);
        beteiligung.getRolle().add(createRolle(rollenbezeichnungValue, String.valueOf(beteiligtennummer)));

        return beteiligung;
    }

    private TypeGDSBeteiligung.Rolle createRolle(String rollenbezeichnungValue, String rollennummer) {
        final TypeGDSBeteiligung.Rolle rolle = new TypeGDSBeteiligung.Rolle();

        rolle.setRollenbezeichnung(
                createCodeFromValue(
                        CodeGDSRollenbezeichnungTyp3.class,
                        rollenbezeichnungValue,
                        ROLE_CODELIST_PATH));
        rolle.setRollennummer(rollennummer);

        return rolle;
    }

    public TypeGDSNatuerlichePerson createNatuerlichePerson(
            String vorname,
            String nachname,
            String title,
            String strasseHausnummer,
            String plz,
            String ort,
            String land) {

        final TypeGDSNatuerlichePerson natuerlichePerson = new TypeGDSNatuerlichePerson();

        // name
        final TypeGDSNameNatuerlichePerson name = new TypeGDSNameNatuerlichePerson();
        name.setVorname(vorname);
        name.setNachname(nachname);
        name.setTitel(title);
        natuerlichePerson.setVollerName(name);

        // anschrift
        natuerlichePerson.getAnschrift().add(
                createAnschrift(strasseHausnummer, plz, ort, land));

        return natuerlichePerson;
    }

    public TypeGDSOrganisation createOrganisation(
            String name,
            String strasseHausnummer,
            String plz,
            String ort,
            String land) {

        final TypeGDSOrganisation organisation = new TypeGDSOrganisation();

        final TypeGDSOrganisation.Bezeichnung bezeichnung = new TypeGDSOrganisation.Bezeichnung();
        bezeichnung.setBezeichnungAktuell(name);
        organisation.setBezeichnung(bezeichnung);

        organisation.getAnschrift().add(
                createAnschrift(strasseHausnummer, plz, ort, land));

        return organisation;
    }

    private TypeGDSAnschrift createAnschrift(
            String strasseHausnummer,
            String plz,
            String ort,
            String land) {

        final TypeGDSAnschrift anschrift = new TypeGDSAnschrift();
        anschrift.setStrasse(strasseHausnummer);
        anschrift.setPostleitzahl(plz);
        anschrift.setOrt(ort);

        // TODO: CodeGDSStaatenTyp3 not on XRepository
        // anschrift.setStaat(createCodeFromValue(
        // CodeGDSStaatenTyp3.class,
        // land,
        // COUNTRY_CODELIST_PATH));

        return anschrift;
    }
}
