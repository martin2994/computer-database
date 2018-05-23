package com.excilys.cdb.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;


public class DateMapperTest {

    /**
     * Teste le cas normal de la fonction convertTimestampLocal.
     */
    @Test
    public void testConvertTimestampToLocal() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LocalDate localDate = DateMapper.timeStampToLocal(timestamp);
        assertTrue(localDate.equals(LocalDate.now()));
    }

    /**
     * Teste la fonction convertTimestampLocal quand le parametre est null.
     */
    @Test
    public void testConvertTimestampToLocalNull() {
        LocalDate localDate = DateMapper.timeStampToLocal(null);
        assertNull(localDate);
    }

    /**
     * Teste le cas normal de la fonction convertLocalDateToTimestamp.
     */
    @Test
    public void testConvertLocalDateToTimestamp() {
        Timestamp timestamp = DateMapper.localDateToTimeStamp(LocalDate.now());
        assertTrue(timestamp.toLocalDateTime().toLocalDate().equals(LocalDate.now()));
    }

    /**
     * Teste la fonction convertLocalDateToTimestamp quand le parametre est null.
     */
    @Test
    public void testConvertLocalDateToTimestampNull() {
        Timestamp sTimestamp = DateMapper.localDateToTimeStamp(null);
        assertNull(sTimestamp);
    }

}
