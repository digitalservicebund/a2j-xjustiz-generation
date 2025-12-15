package de.digitalservice.model.fgrUser;

import java.util.List;

import de.digitalservice.model.common.Anrede;
import de.digitalservice.model.common.Title;
import de.digitalservice.model.common.YesNoAnswer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserData {

    // --- Basic flight details ---
    private String direktFlugnummer;
    private String buchungsNummer;
    private String direktAbflugsDatum;
    private String direktAbflugsZeit;
    private String direktAnkunftsDatum;
    private String direktAnkunftsZeit;
    private String startAirport;
    private String endAirport;

    // --- Actual flight info ---
    private YesNoAnswer tatsaechlicherFlug;
    private String tatsaechlicherAnkunftsDatum;
    private String tatsaechlicherAnkunftsZeit;

    // --- Replacement flight / connection details ---
    private ErsatzverbindungArt ersatzverbindungArt;
    private String ersatzflug;
    private String andereErsatzverbindungBeschreibung;
    private String andereErsatzverbindungAnkunftsDatum;
    private String andereErsatzverbindungAnkunftsZeit;

    private String ersatzFlugnummer;
    private String ersatzFlugAnkunftsDatum;
    private String ersatzFlugAnkunftsZeit;

    // --- Airline info ---
    private String fluggesellschaft;
    private String fluggesellschaftStrasseHausnummer;
    private String fluggesellschaftPostleitzahl;
    private String fluggesellschaftOrt;
    private String fluggesellschaftLand;

    // --- Connection stops ---
    private ZwischenstoppAnzahl zwischenstoppAnzahl;
    private VerspaeteterFlug verspaeteterFlug;
    private YesNoAnswer anschlussFlugVerpasst;

    private String ersterZwischenstopp;
    private String zweiterZwischenstopp;
    private String dritterZwischenstopp;

    // --- Additional info ---
    private String bereich;
    private String zusaetzlicheAngaben;

    // --- Cancellation / Replacement flight details ---
    private String annullierungErsatzverbindungFlugnummer;
    private String annullierungErsatzverbindungAbflugsDatum;
    private String annullierungErsatzverbindungAbflugsZeit;
    private String annullierungErsatzverbindungAnkunftsDatum;
    private String annullierungErsatzverbindungAnkunftsZeit;

    // personal Data

    private Anrede anrede;
    private Title title;
    private String vorname;
    private String nachname;
    private String strasseHausnummer;
    private String plz;
    private String ort;
    private String land;
    private String telefonnummer;
    private String iban;
    private String kontoinhaber;

    // court data
    private String courtName;

    private String antrag;

    // court procedure

    private YesNoAnswer hasZeugen;
    private YesNoAnswer versaeumnisurteil;
    private Videoverhandlung videoverhandlung;
    private YesNoAnswer prozesszinsen;

    // weitere Personen
    private YesNoAnswer isWeiterePersonen;
    private List<WeiterePerson> weiterePersonen;
}
