package de.digitalservice.service.utils;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class XmlDateConverter {
    public static XMLGregorianCalendar toXmlGregorianCalendar(
            String date,
            String time) {
        try {
            if (date == null || date.isBlank()) {
                throw new IllegalArgumentException("Date must not be null or blank");
            }

            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

            // Parse date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalDate localDate = LocalDate.parse(date, dateFormatter);

            // No time → return xs:date
            if (time == null || time.isBlank()) {
                return datatypeFactory.newXMLGregorianCalendarDate(
                        localDate.getYear(),
                        localDate.getMonthValue(),
                        localDate.getDayOfMonth(),
                        DatatypeConstants.FIELD_UNDEFINED);
            }

            // Parse time → HH:mm or HH:mm:ss
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm[:ss]");

            LocalTime localTime = LocalTime.parse(time, timeFormatter);

            return datatypeFactory.newXMLGregorianCalendar(
                    localDate.getYear(),
                    localDate.getMonthValue(),
                    localDate.getDayOfMonth(),
                    localTime.getHour(),
                    localTime.getMinute(),
                    localTime.getSecond(),
                    DatatypeConstants.FIELD_UNDEFINED, // milliseconds
                    DatatypeConstants.FIELD_UNDEFINED // timezone
            );

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid date/time: date='" + date + "', time='" + time + "'",
                    e);
        }
    }

}
