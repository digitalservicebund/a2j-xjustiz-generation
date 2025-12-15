package de.digitalservice.model.fgrUser;

import de.digitalservice.model.common.Anrede;
import de.digitalservice.model.common.Title;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WeiterePerson {

    private Anrede anrede;
    private Title title;
    private String vorname;
    private String nachname;
    private String strasseHausnummer;
    private String plz;
    private String ort;
    private String land;
    private String telefonnummer;
    private String buchungsnummer;

}
