package de.digitalservice.service.claims;

import static de.digitalservice.codes.CodeUtils.createCodeFromValue;

import de.digitalservice.model.common.YesNoAnswer;
import de.digitalservice.model.fgrUser.Bereich;
import de.digitalservice.model.fgrUser.ErsatzverbindungArt;
import de.digitalservice.model.fgrUser.UserData;
import de.digitalservice.model.fgrUser.ZwischenstoppAnzahl;
import de.digitalservice.service.utils.XmlDateConverter;
import de.digitalservice.service.utils.YesNoAnswerToBooleanConverter;
import de.xjustiz.CodeKLAVERAnspruchsartTyp3;
import de.xjustiz.CodeKLAVERFGRAnspruchshoeheTyp3;
import de.xjustiz.TypeGDSRefRollennummer;
import de.xjustiz.TypeKLAVERFGRAusgleichsansprueche;
import de.xjustiz.TypeKLAVERFGRBefoerderung;
import de.xjustiz.TypeKLAVERFGRBetroffenerFlug;
import de.xjustiz.TypeKLAVERFGRFluggast;
import de.xjustiz.TypeKLAVERFGRAusgleichsansprueche.Anspruchshoehe;
import de.xjustiz.TypeKLAVERFGRAusgleichsansprueche.AuswahlLeistungsstoerung;

public class FgrBegruendetheit {

    private static final String CODE_ANSPRUCHSHOEHE_PATH = "codes/KLAVER.FGR.Anspruchshoehe_1.0.xml";
    private final TypeKLAVERFGRAusgleichsansprueche ausgleichsansprueche;

    public TypeKLAVERFGRAusgleichsansprueche getAusgleichsansprueche() {
        return ausgleichsansprueche;
    }

    public FgrBegruendetheit() {
        this.ausgleichsansprueche = new TypeKLAVERFGRAusgleichsansprueche();
    }

    public void createFgrBegruendetheit(UserData userData, TypeGDSRefRollennummer fluggastRollennummer) {

        ausgleichsansprueche.setGeltungsbereich(createGeltungsbereich());
        ausgleichsansprueche.getFluggast().add(createFluggast(userData, fluggastRollennummer));
        ausgleichsansprueche.setGeplanteReise(createGeplanteReise(userData));
        ausgleichsansprueche.getAuswahlLeistungsstoerung()
                .add(createLeistungsstoerung(userData));
        ausgleichsansprueche.setAnspruchshoehe(createAnspruchshoehe(userData));
    }

    private TypeKLAVERFGRAusgleichsansprueche.Geltungsbereich createGeltungsbereich() {
        var geltungsbereich = new TypeKLAVERFGRAusgleichsansprueche.Geltungsbereich();

        // TODO: voraussetzungenFlugtyp has option for Beweis
        var voraussetzungenFlugtyp = new TypeKLAVERFGRAusgleichsansprueche.Geltungsbereich.VoraussetzungenFlugtyp();
        voraussetzungenFlugtyp.setFesteTragflaechenUndMotorisiert(true);
        geltungsbereich.setVoraussetzungenFlugtyp(voraussetzungenFlugtyp);

        // TODO: kostenFlugticket has options for Beweis
        var kostenFlugticket = new TypeKLAVERFGRAusgleichsansprueche.Geltungsbereich.KostenFlugticket();
        kostenFlugticket.setNichtKostenlos(true);
        kostenFlugticket.setKeinReduzierterTarif(true);
        geltungsbereich.setKostenFlugticket(kostenFlugticket);

        return geltungsbereich;
    }

    private TypeKLAVERFGRFluggast createFluggast(UserData userData,
            TypeGDSRefRollennummer fluggastRollennummer) {

        // TODO: add possibility for various plaintiffs
        // TODO: fluggast has option for Beweis
        var fluggast = new TypeKLAVERFGRFluggast();
        fluggast.setBuchungscode(userData.getBuchungsNummer());
        fluggast.setFluggast(fluggastRollennummer);
        return fluggast;
    }

    private TypeKLAVERFGRAusgleichsansprueche.GeplanteReise createGeplanteReise(UserData userData) {

        var geplanteReise = new TypeKLAVERFGRAusgleichsansprueche.GeplanteReise();

        var start = new TypeKLAVERFGRAusgleichsansprueche.GeplanteReise.Start();
        start.setFlughafen(userData.getStartAirport());
        start.setZeitpunkt(XmlDateConverter.toXmlGregorianCalendar(
                userData.getDirektAbflugsDatum(),
                userData.getDirektAbflugsZeit()));
        geplanteReise.setStart(start);

        addZwischenstopps(geplanteReise, userData);

        var ankunft = new TypeKLAVERFGRAusgleichsansprueche.GeplanteReise.Ankunft();
        ankunft.setFlughafen(userData.getEndAirport());
        ankunft.setZeitpunkt(XmlDateConverter.toXmlGregorianCalendar(
                userData.getDirektAnkunftsDatum(),
                userData.getDirektAnkunftsZeit()));
        geplanteReise.setAnkunft(ankunft);

        // TODO: Geplante Reise can have Beweis
        return geplanteReise;
    }

    private void addZwischenstopps(
            TypeKLAVERFGRAusgleichsansprueche.GeplanteReise geplanteReise,
            UserData userData) {

        if (userData.getZwischenstoppAnzahl() == null
                || userData.getZwischenstoppAnzahl() == ZwischenstoppAnzahl.NONE) {
            return;
        }

        var zwischenstopps = geplanteReise.getZwischenstopp();

        addIfPresent(zwischenstopps, userData.getErsterZwischenstopp());
        addIfPresent(zwischenstopps, userData.getZweiterZwischenstopp());
        addIfPresent(zwischenstopps, userData.getDritterZwischenstopp());
    }

    private void addIfPresent(java.util.List<String> list, String value) {
        if (value != null && !value.isEmpty()) {
            list.add(value);
        }
    }

    public AuswahlLeistungsstoerung createLeistungsstoerung(UserData userData) {

        var auswahlLeistungsstoerung = new AuswahlLeistungsstoerung();

        if (userData.getBereich() == null || userData.getBereich().isEmpty()) {
            return auswahlLeistungsstoerung;
        }

        var betroffenerFlug = createBetroffenerFlug(userData);

        if (Bereich.DELAYED.getValue().equals(userData.getBereich())) {
            auswahlLeistungsstoerung.setVerspaetung(
                    createVerspaetung(userData, betroffenerFlug));
        }

        if (Bereich.CANCELLED.getValue().equals(userData.getBereich())) {
            auswahlLeistungsstoerung.setAnnullierung(
                    createAnnullierung(userData, betroffenerFlug));
        }

        return auswahlLeistungsstoerung;
    }

    private TypeKLAVERFGRBetroffenerFlug createBetroffenerFlug(UserData userData) {
        var flug = new TypeKLAVERFGRBetroffenerFlug();
        flug.setFlugnummer(userData.getBetroffenerFlugFlugnummer());
        flug.setAbflugFlughafen(userData.getBetroffenerFlugAbflugFlughafen());
        flug.setAnkunftFlughafen(userData.getBetroffenerFlugAnkunftFlughafen());
        return flug;
    }

    private AuswahlLeistungsstoerung.Verspaetung createVerspaetung(
            UserData userData,
            TypeKLAVERFGRBetroffenerFlug betroffenerFlug) {

        var verspaetung = new AuswahlLeistungsstoerung.Verspaetung();
        verspaetung.setBetroffenerFlug(betroffenerFlug);
        verspaetung.setAnkunft(createAnkunft(userData));
        verspaetung.setAnschlussflugVerpasst(
                YesNoAnswerToBooleanConverter.convert(userData.getAnschlussFlugVerpasst()));
        // TODO: verspaetung can have Beweis
        return verspaetung;
    }

    private AuswahlLeistungsstoerung.Annullierung createAnnullierung(
            UserData userData,
            TypeKLAVERFGRBetroffenerFlug betroffenerFlug) {

        var annullierung = new AuswahlLeistungsstoerung.Annullierung();
        annullierung.setBetroffenerFlug(betroffenerFlug);
        annullierung.setAnschlussflugVerpasst(
                YesNoAnswerToBooleanConverter.convert(userData.getAnschlussFlugVerpasst()));
        // TODO: add ersatzangebot
        return annullierung;
    }

    private TypeKLAVERFGRBefoerderung createAnkunft(UserData userData) {

        var ankunft = new TypeKLAVERFGRBefoerderung();
        var befoerderungsart = new TypeKLAVERFGRBefoerderung.AuswahlBefoerderungsart();
        ankunft.setAuswahlBefoerderungsart(befoerderungsart);

        if (userData.getTatsaechlicherFlug() == YesNoAnswer.NO) {

            if (userData.getErsatzverbindungArt() == ErsatzverbindungArt.FLIGHT) {
                ankunft.getAuswahlBefoerderungsart().setFlugnummer(userData.getErsatzFlugnummer());
                ankunft.setZeitpunkt(XmlDateConverter.toXmlGregorianCalendar(
                        userData.getErsatzFlugAnkunftsDatum(),
                        userData.getErsatzFlugAnkunftsZeit()));
            }

            if (userData.getErsatzverbindungArt() == ErsatzverbindungArt.OTHER) {
                ankunft.getAuswahlBefoerderungsart()
                        .setSonstige(userData.getAndereErsatzverbindungBeschreibung());
                ankunft.setZeitpunkt(XmlDateConverter.toXmlGregorianCalendar(
                        userData.getAndereErsatzverbindungAnkunftsDatum(),
                        userData.getAndereErsatzverbindungAnkunftsZeit()));
            }

            return ankunft;
        }

        ankunft.getAuswahlBefoerderungsart()
                .setFlugnummer(userData.getDirektFlugnummer());
        ankunft.setZeitpunkt(XmlDateConverter.toXmlGregorianCalendar(
                userData.getTatsaechlicherAnkunftsDatum(),
                userData.getTatsaechlicherAnkunftsZeit()));

        return ankunft;
    }

    private Anspruchshoehe createAnspruchshoehe(UserData userData) {
        var anspruchshoehe = new Anspruchshoehe();
        String distanceValue = userData.getDistance() == null ? null : userData.getDistance().getValue();
        anspruchshoehe.setAnspruchshoehe(
                createCodeFromValue(CodeKLAVERFGRAnspruchshoeheTyp3.class, distanceValue, CODE_ANSPRUCHSHOEHE_PATH));
        return anspruchshoehe;
    }
}
