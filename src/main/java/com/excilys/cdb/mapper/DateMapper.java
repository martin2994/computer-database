package com.excilys.cdb.mapper;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Permet de transformer les dates Timestamp SQL en date LocalTime.
 * @author martin
 *
 */
public class DateMapper {

    /**
     * Convertit un Timestamp en une LocalDate.
     * @param time
     *            Timestamp à convertir
     * @return LocalDate la date en LocalDate
     */
    public static LocalDate convertTimeStampToLocal(final Timestamp time) {
        if (time != null) {
            return time.toLocalDateTime().toLocalDate();
        }
        return null;

    }

    /**
     * Convertit une LocalDate en un Timestamp.
     * @param date
     *            LocalDate à convertir
     * @return TimeStamp la date en TimeStamp
     */
    public static Timestamp convertLocalDateToTimeStamp(final LocalDate date) {
        if (date != null) {
            return Timestamp.valueOf(date.atStartOfDay());
        }
        return null;

    }

}
