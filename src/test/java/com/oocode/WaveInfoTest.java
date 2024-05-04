package com.oocode;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class WaveInfoTest {

    @Test
    public void throwsExceptionOnInvalidWaveSize() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new WaveInfo(
                        "Location A",
                        LocalDateTime.of(2000, Month.APRIL, 2, 0,0),
                        "-3.000"
                )
        );
        assertEquals("Wave size must be greater than or equal to zero.", exception.getMessage());
    }

    @Test
    public void throwsExceptionOnSmallInvalidWaveSize() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new WaveInfo(
                        "Location A",
                        LocalDateTime.of(2000, Month.APRIL, 2, 0,0),
                        "-0.001"
                )
        );
        assertEquals("Wave size must be greater than or equal to zero.", exception.getMessage());
    }

    @Test
    public void returnsCorrectDayOfWeek() {
        WaveInfo waveInfo = new WaveInfo(
                "Location XY",
                LocalDateTime.of(2024, Month.MAY, 4, 0,0),
                "2.000"
        );
        assertEquals("Saturday", waveInfo.getDayOfWeek());
    }

    @Test
    public void returnsCorrectDayOfWeekWeekday() {
        WaveInfo waveInfo = new WaveInfo(
                "Location XY",
                LocalDateTime.of(2024, Month.MAY, 2, 0,0),
                "2.000"
        );
        assertEquals("Thursday", waveInfo.getDayOfWeek());
    }
}
