package de.digitalservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;
import de.digitalservice.model.common.Anrede;
import de.digitalservice.model.fgrUser.Bereich;
import de.digitalservice.model.fgrUser.Distance;
import de.digitalservice.model.fgrUser.ErsatzverbindungArt;
import de.digitalservice.model.fgrUser.WeiterePerson;
import de.digitalservice.model.fgrUser.ZwischenstoppAnzahl;

@Data
public class FlightClaimRequest {

        // ===== Flight details =====

        @Schema(description = "IATA-Code des Abflughafens", example = "BER", requiredMode = Schema.RequiredMode.REQUIRED)
        private String startAirport;

        @Schema(description = "Art der Flugstörung gemäß Fluggastrechteverordnung", example = "verspaetet", allowableValues = {
                        "verspaetet", "annullierung",
                        "nichtbefoerderung" }, requiredMode = Schema.RequiredMode.REQUIRED)
        private Bereich bereich;

        // TODO: change to name of airline
        @Schema(description = "IATA-Code der Fluggesellschaft", example = "LH")
        private String fluggesellschaft;

        @Schema(description = "Ob Prozesszinsen geltend gemacht werden sollen", example = "true")
        private Boolean prozesszinsen;

        @Schema(description = "Ob ein Versäumnisurteil beantragt wird", example = "true")
        private Boolean versaeumnisurteil;

        @Schema(description = "Direkte Flugnummer der ursprünglichen Buchung", example = "AB6303")
        private String direktFlugnummer;

        @Schema(description = "Buchungsnummer (PNR oder Ticketnummer)", example = "AB6303")
        private String buchungsNummer;

        @Schema(description = "Geplantes Abflugdatum des ursprünglichen Fluges (TT.MM.JJJJ)", example = "10.10.2023")
        private String direktAbflugsDatum;

        @Schema(description = "Geplante Abflugzeit des ursprünglichen Fluges (HH:mm)", example = "09:00")
        private String direktAbflugsZeit;

        @Schema(description = "Anzahl der Zwischenstopps der ursprünglichen Verbindung", example = "threeStop", allowableValues = {
                        "noStop", "oneStop", "twoStop", "threeStop" })
        private ZwischenstoppAnzahl zwischenstoppAnzahl;

        @Schema(description = "IATA-Code des ersten Zwischenstopps", example = "SSA")
        private String ersterZwischenstopp;

        @Schema(description = "IATA-Code des zweiten Zwischenstopps", example = "NUE")
        private String zweiterZwischenstopp;

        @Schema(description = "IATA-Code des dritten Zwischenstopps", example = "FRA")
        private String dritterZwischenstopp;

        @Schema(description = "Geplantes Ankunftsdatum des ursprünglichen Fluges", example = "10.10.2023")
        private String direktAnkunftsDatum;

        @Schema(description = "Geplante Ankunftszeit des ursprünglichen Fluges", example = "10:00")
        private String direktAnkunftsZeit;

        @Schema(description = "IATA-Code des Ziel-Flughafens", example = "JFK", requiredMode = Schema.RequiredMode.REQUIRED)
        private String endAirport;

        @Schema(description = "Ob der Flug tatsächlich durchgeführt wurde", example = "false")
        private Boolean tatsaechlicherFlug;

        // ===== Ersatzverbindung =====

        @Schema(description = "Art der Ersatzbeförderung", example = "flug", allowableValues = { "flug", "bahn",
                        "bus" })
        private ErsatzverbindungArt ersatzverbindungArt;

        @Schema(description = "Flugnummer der Ersatzverbindung", example = "AB6303")
        private String ersatzFlugnummer;

        @Schema(description = "Ankunftsdatum der Ersatzverbindung", example = "10.02.2024")
        private String ersatzFlugAnkunftsDatum;

        @Schema(description = "Ankunftszeit der Ersatzverbindung", example = "10:10")
        private String ersatzFlugAnkunftsZeit;

        // ===== Additional info =====

        @Schema(description = "Freitextfeld für zusätzliche Angaben des Klägers", example = "Der Flug hatte über 5 Stunden Verspätung.")
        private String zusaetzlicheAngaben;

        // ===== Claimant =====

        @Schema(description = "Anrede der klagenden Person", example = "herr", allowableValues = { "herr", "frau" })
        private Anrede anrede;

        @Schema(description = "Akademischer Titel (optional)", example = "Dr.")
        private String title;

        @Schema(description = "Vorname der klagenden Person", example = "Max", requiredMode = Schema.RequiredMode.REQUIRED)
        private String vorname;

        @Schema(description = "Nachname der klagenden Person", example = "Mustermann", requiredMode = Schema.RequiredMode.REQUIRED)
        private String nachname;

        @Schema(description = "Straße und Hausnummer", example = "Musterstr. 22")
        private String strasseHausnummer;

        @Schema(description = "Postleitzahl", example = "10969")
        private String plz;

        @Schema(description = "Ort", example = "Berlin")
        private String ort;

        @Schema(description = "Land", example = "Deutschland")
        private String land;

        @Schema(description = "Telefonnummer", example = "1010101010")
        private String telefonnummer;

        // ===== Payment =====

        @Schema(description = "IBAN für die Auszahlung", example = "DE68500123456789000000")
        private String iban;

        @Schema(description = "Name des Kontoinhabers", example = "Test-Test Müller")
        private String kontoinhaber;

        // ===== cedents =====

        @Schema(description = "Liste der weiteren mitreisenden Personen, für die eine Abtretung vorliegt")
        private List<WeiterePerson> weiterePersonen;

        // ===== Airline address =====

        @Schema(description = "Straße und Hausnummer der Fluggesellschaft", example = "Musterstr. 30")
        private String fluggesellschaftStrasseHausnummer;

        @Schema(description = "Postleitzahl der Fluggesellschaft", example = "10970")
        private String fluggesellschaftPostleitzahl;

        @Schema(description = "Ort der Fluggesellschaft", example = "Berlin")
        private String fluggesellschaftOrt;

        @Schema(description = "Land der Fluggesellschaft", example = "Deutschland")
        private String fluggesellschaftLand;

        // ===== Legal =====

        @Schema(description = "Streitwert in Euro", example = "450")
        private String streitwert;

        @Schema(description = "Name des zuständigen Gerichts", example = "ZZ Test-Baden-Württemberg")
        private String courtName;

        @Schema(description = "Rechtlicher Anspruchsgegenstand", example = "Ausgleichszahlung nach der Fluggastrechteverordnung (EG) 261/2004")
        private String anspruchsgegenstand;

        @Schema(description = "Hauptantrag des Klägers", example = "Die beklagte Partei wird verurteilt, an die klagende Partei 750 € zu zahlen.")
        private String antrag;

        @Schema(description = "Zinssatz in Prozentpunkten über dem Basiszinssatz", example = "5.0")
        private String zinssatz;

        @Schema(description = "Nebenantrag für Zinsen", example = "Die beklagte Partei wird verurteilt, Zinsen in Höhe von 5 Prozentpunkten über dem jeweiligen Basiszinssatz seit Rechtshängigkeit zu zahlen.")
        private String nebenantragZinsen;

        @Schema(description = "Flugnummer des betroffenen Fluges", example = "AB6303")
        private String betroffenerFlugFlugnummer;

        @Schema(description = "Abflughafen des betroffenen Fluges", example = "FRA")
        private String betroffenerFlugAbflugFlughafen;

        @Schema(description = "Ankunftsflughafen des betroffenen Fluges", example = "JFK")
        private String betroffenerFlugAnkunftFlughafen;

        @Schema(description = "Entfernungskategorie gemäß Fluggastrechteverordnung", example = "entfernungueber3500", allowableValues = {
                        "entfernungunter1500", "entfernung1500bis3500", "entfernungueber3500" })
        private Distance distance;
}
