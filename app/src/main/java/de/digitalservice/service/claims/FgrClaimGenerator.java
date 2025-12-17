package de.digitalservice.service.claims;

import java.math.BigInteger;
import java.util.List;

import de.digitalservice.model.common.Title;
import de.digitalservice.model.fgrUser.UserData;
import de.digitalservice.model.fgrUser.WeiterePerson;
import de.digitalservice.service.GrunddatenGenerator;
import de.digitalservice.service.utils.RoleNumberRegistry;
import de.digitalservice.service.utils.XmlDateConverter;
import de.xjustiz.CodeGDSZinsmethodeTyp3;
import de.xjustiz.NachrichtKlaverKlageverfahren3500001;
import de.xjustiz.TypeGDSGrunddaten;
import de.xjustiz.TypeGDSRefRollennummer;
import de.xjustiz.TypeGDSZinsen;
import de.xjustiz.TypeKLAVERAntrag;

import static de.digitalservice.codes.CodeUtils.createCodeFromValue;

public class FgrClaimGenerator {

        private static final BigInteger ANSPRUCHSNUMMER_HAUPTANTRAG = BigInteger.ONE;
        private static final BigInteger ANSPRUCHSNUMMER_NEBENANTRAG_ZINSEN = BigInteger.TWO;

        private static final String ROLE_PLAINTIFF = "Kläger(in)";
        private static final String ROLE_CEDENT = "Zedent(in)";
        private static final String ROLE_DEFENDANT = "Beklagte(r)";
        private static final String ZINSMETHODE = "jährlicher Zinssatz Über Basiszins";
        private static final String CODE_ZINSMETHODE_PATH = "codes/GDS.Zinsmethode_1.0.xml";

        private final NachrichtKlaverKlageverfahren3500001.Inhaltsdaten inhaltsdaten;
        private final GrunddatenGenerator grunddatenGenerator;
        private final RoleNumberRegistry roleNumbers;

        private int rollennummerPlaintiff;
        private int rollennummerDefendant;

        public FgrClaimGenerator() {
                this.inhaltsdaten = new NachrichtKlaverKlageverfahren3500001.Inhaltsdaten();
                this.grunddatenGenerator = new GrunddatenGenerator();
                this.roleNumbers = new RoleNumberRegistry();
        }

        // ---------------------------------------------------------------------
        // Claim
        // ---------------------------------------------------------------------

        public NachrichtKlaverKlageverfahren3500001.Inhaltsdaten createClaim(UserData userData) {

                final var antrag = new TypeKLAVERAntrag();
                final var sachantraege = new TypeKLAVERAntrag.Sachantraege();

                sachantraege.setInhalt(userData.getAntrag());

                sachantraege.getAnspruch().add(
                                new AnspruchBuilder()
                                                .fortlaufendeNummer(ANSPRUCHSNUMMER_HAUPTANTRAG)
                                                .anspruchsart("Zahlung")
                                                .anspruchssteller(rollennummerPlaintiff)
                                                .anspruchsgegner(rollennummerDefendant)
                                                .anspruchsgegenstand(userData.getAnspruchsgegenstand())
                                                .build());

                antrag.setSachantraege(sachantraege);

                // Nebenantrag Zinsen
                // TODO add if clause to only add if user provided data
                antrag.setNebenantraegeZinsen(createNebenantragZinsen(userData));

                this.inhaltsdaten.setAntraege(antrag);

                var fgrBegruendetheit = new FgrBegruendetheit();
                var refRollenNummerPlaintiff = new TypeGDSRefRollennummer();
                refRollenNummerPlaintiff.setRefRollennummer(String.valueOf(rollennummerPlaintiff));
                fgrBegruendetheit.createFgrBegruendetheit(userData, refRollenNummerPlaintiff);

                var auswahl = new NachrichtKlaverKlageverfahren3500001.Inhaltsdaten.AuswahlBegruendetheit();
                auswahl.setFluggastrechteAusgleichsansprueche(fgrBegruendetheit.getAusgleichsansprueche());
                this.inhaltsdaten.setAuswahlBegruendetheit(auswahl);

                return inhaltsdaten;
        }

        private TypeKLAVERAntrag.NebenantraegeZinsen createNebenantragZinsen(UserData userData) {

                final var nebenantragZinsen = new TypeKLAVERAntrag.NebenantraegeZinsen();
                final var zinsanspruch = new TypeKLAVERAntrag.NebenantraegeZinsen.Zinsanspruch();

                zinsanspruch.setFortlaufendeNummer(ANSPRUCHSNUMMER_NEBENANTRAG_ZINSEN);
                zinsanspruch.setRefFortlaufendeNummer(ANSPRUCHSNUMMER_HAUPTANTRAG);

                final var zinsen = new TypeGDSZinsen();
                // TODO check if original departure date is the accurate field for zinsbeginn
                // (from a legal perspective)
                zinsen.setZinsbeginn(
                                XmlDateConverter.toXmlGregorianCalendar(
                                                userData.getDirektAbflugsDatum(), null));

                zinsen.setZinssatz(userData.getZinssatz());
                zinsen.setZinsmethode(
                                createCodeFromValue(
                                                CodeGDSZinsmethodeTyp3.class,
                                                ZINSMETHODE,
                                                CODE_ZINSMETHODE_PATH));

                zinsanspruch.getZinsen().add(zinsen);
                nebenantragZinsen.getZinsanspruch().add(zinsanspruch);
                nebenantragZinsen.setInhalt(userData.getNebenantragZinsen());

                return nebenantragZinsen;
        }

        // ---------------------------------------------------------------------
        // Grunddaten
        // ---------------------------------------------------------------------

        public TypeGDSGrunddaten generatePlaintiffAndCedentGrunddaten(UserData userData) {

                // Kläger
                rollennummerPlaintiff = roleNumbers.next();

                final var plaintiff = grunddatenGenerator.createNatuerlichePerson(
                                userData.getVorname(),
                                userData.getNachname(),
                                titleOrNull(userData.getTitle()),
                                userData.getStrasseHausnummer(),
                                userData.getPlz(),
                                userData.getOrt(),
                                userData.getLand());

                grunddatenGenerator.addGrunddaten(
                                grunddatenGenerator.createBeteiligungForNatuerlichePerson(
                                                rollennummerPlaintiff,
                                                ROLE_PLAINTIFF,
                                                plaintiff));

                // Zedenten
                List<WeiterePerson> weiterePersonen = userData.getWeiterePersonen();
                if (weiterePersonen == null || weiterePersonen.isEmpty()) {
                        return grunddatenGenerator.getGrunddaten();
                }

                for (WeiterePerson person : weiterePersonen) {

                        int rollennummerZedent = roleNumbers.next();

                        final var natuerlichePerson = grunddatenGenerator.createNatuerlichePerson(
                                        person.getVorname(),
                                        person.getNachname(),
                                        titleOrNull(person.getTitle()),
                                        person.getStrasseHausnummer(),
                                        person.getPlz(),
                                        person.getOrt(),
                                        person.getLand());

                        grunddatenGenerator.addGrunddaten(
                                        grunddatenGenerator.createBeteiligungForNatuerlichePerson(
                                                        rollennummerZedent,
                                                        ROLE_CEDENT,
                                                        natuerlichePerson));
                }

                return grunddatenGenerator.getGrunddaten();
        }

        public TypeGDSGrunddaten generateDefendantGrunddaten(UserData userData) {

                rollennummerDefendant = roleNumbers.next();

                final var defendant = grunddatenGenerator.createOrganisation(
                                // TODO: get full airline name (e.g. currently LH for Lufthansa)
                                userData.getFluggesellschaft(),
                                userData.getFluggesellschaftStrasseHausnummer(),
                                userData.getFluggesellschaftPostleitzahl(),
                                userData.getFluggesellschaftOrt(),
                                userData.getFluggesellschaftLand());

                grunddatenGenerator.addGrunddaten(
                                grunddatenGenerator.createBeteiligungForOrganisation(
                                                rollennummerDefendant,
                                                ROLE_DEFENDANT,
                                                defendant));

                return grunddatenGenerator.getGrunddaten();
        }

        private static String titleOrNull(Title title) {
                return title != null ? title.getValue() : null;
        }

        public GrunddatenGenerator getGrunddatenGenerator() {
                return grunddatenGenerator;
        }
}
