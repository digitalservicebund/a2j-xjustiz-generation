package de.digitalservice.service.claims;

import java.math.BigInteger;
import java.util.List;

import de.digitalservice.model.fgrUser.UserData;
import de.digitalservice.model.fgrUser.WeiterePerson;
import de.digitalservice.service.GrunddatenGenerator;
import de.xjustiz.NachrichtKlaverKlageverfahren3500001;
import de.xjustiz.TypeGDSGrunddaten;
import de.xjustiz.TypeKLAVERAntrag;
import de.xjustiz.TypeKLAVERAnspruch;

public class FgrClaimGenerator {

    private static final BigInteger FIRST_ANSPRUCH_NUMMER = BigInteger.ONE;

    private static final String ROLE_PLAINTIFF = "Kläger(in)";
    private static final String ROLE_CEDENT = "Zedent(in)";
    private static final String ROLE_DEFENDANT = "Beklagte(r)";

    private final NachrichtKlaverKlageverfahren3500001 klage;
    private final GrunddatenGenerator grunddatenGenerator;

    public FgrClaimGenerator() {
        this.klage = new NachrichtKlaverKlageverfahren3500001();
        this.grunddatenGenerator = new GrunddatenGenerator();
    }

    public NachrichtKlaverKlageverfahren3500001 createAntrag(UserData userData) {

        var inhaltsdaten = new NachrichtKlaverKlageverfahren3500001.Inhaltsdaten();

        var antrag = new TypeKLAVERAntrag();
        var sachantraege = new TypeKLAVERAntrag.Sachantraege();

        var anspruch = new TypeKLAVERAnspruch();
        anspruch.setFortlaufendeNummer(FIRST_ANSPRUCH_NUMMER);

        sachantraege.setInhalt(userData.getAntrag());

        antrag.setSachantraege(sachantraege);
        inhaltsdaten.setAntraege(antrag);
        klage.setInhaltsdaten(inhaltsdaten);

        return klage;
    }

    public TypeGDSGrunddaten generatePlaintiffAndCedentGrunddaten(UserData userData) {

        // Kläger
        var plaintiff = grunddatenGenerator.createNatuerlichePerson(
                userData.getVorname(),
                userData.getNachname(),
                // TODO: use title when available
                null,
                userData.getStrasseHausnummer(),
                userData.getPlz(),
                userData.getOrt(),
                userData.getLand());

        grunddatenGenerator.addGrunddaten(
                grunddatenGenerator.createBeteiligungForNatuerlichePerson(
                        1,
                        ROLE_PLAINTIFF,
                        plaintiff));

        // Zedenten
        List<WeiterePerson> weiterePersonen = userData.getWeiterePersonen();
        if (weiterePersonen == null || weiterePersonen.isEmpty()) {
            return grunddatenGenerator.getGrunddaten();
        }

        for (int i = 0; i < weiterePersonen.size(); i++) {
            WeiterePerson person = weiterePersonen.get(i);

            var natuerlichePerson = grunddatenGenerator.createNatuerlichePerson(
                    person.getVorname(),
                    person.getNachname(),
                    // TODO: use title when available
                    null,
                    person.getStrasseHausnummer(),
                    person.getPlz(),
                    person.getOrt(),
                    person.getLand());

            grunddatenGenerator.addGrunddaten(
                    grunddatenGenerator.createBeteiligungForNatuerlichePerson(
                            i + 2,
                            ROLE_CEDENT,
                            natuerlichePerson));
        }

        return grunddatenGenerator.getGrunddaten();
    }

    public TypeGDSGrunddaten generateDefendantGrunddaten(UserData userData) {

        var defendant = grunddatenGenerator.createOrganisation(
                userData.getFluggesellschaft(),
                userData.getFluggesellschaftStrasseHausnummer(),
                userData.getFluggesellschaftPostleitzahl(),
                userData.getFluggesellschaftOrt(),
                userData.getFluggesellschaftLand());

        int beteiligtennummer = grunddatenGenerator.getGrunddaten()
                .getVerfahrensdaten()
                .getBeteiligung()
                .size() + 1;

        grunddatenGenerator.addGrunddaten(
                grunddatenGenerator.createBeteiligungForOrganisation(
                        beteiligtennummer,
                        ROLE_DEFENDANT,
                        defendant));

        return grunddatenGenerator.getGrunddaten();
    }

    public GrunddatenGenerator getGrunddatenGenerator() {
        return grunddatenGenerator;
    }
}
