package com.oocode;

import com.oocode.utils.SurfConditionsHelper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SurfConditionsTest {

    @Test
    public void throwsExceptionOnInvalidWaveSize() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SurfConditionsHelper.createSurfConditions(
                        "Location A",
                        1,
                        2,
                        LocalDateTime.of(2000, Month.APRIL, 2, 0, 0),
                        "-3.000"
                )
        );
        assertEquals("Wave size must be greater than or equal to zero.", exception.getMessage());
    }

    @Test
    public void throwsExceptionOnSmallInvalidWaveSize() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SurfConditionsHelper.createSurfConditions(
                        "Location A",
                        0,
                        0,
                        LocalDateTime.of(2000, Month.APRIL, 2, 0, 0),
                        "-0.001"
                )
        );
        assertEquals("Wave size must be greater than or equal to zero.", exception.getMessage());
    }

    @Test
    public void returnsCorrectDayOfWeek() {
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location XY",
                175.4,
                23,
                LocalDateTime.of(2024, Month.MAY, 4, 0, 0),
                "2.000"
        );
        assertEquals("Saturday", surfConditions.getDayOfWeek());
    }


    @Test
    public void returnsCorrectDayOfWeekWeekday() {
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location XY",
                2.345,
                7.58,
                LocalDateTime.of(2024, Month.MAY, 2, 0, 0),
                "2.000"
        );
        assertEquals("Thursday", surfConditions.getDayOfWeek());
    }

    @Test
    public void returnsCorrectDate() {
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.MAY, 2, 0, 0);
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location XY",
                0.5,
                123.4,
                dateTime,
                "2.000"
        );
        assertEquals(surfConditions.getDate(), dateTime.toLocalDate());
    }

    @Test
    public void returnsCorrectOlderDate() {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JANUARY, 6, 0, 0);
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location XY",
                1,
                2,
                dateTime,
                "2.000"
        );
        assertEquals(surfConditions.getDate(), dateTime.toLocalDate());
    }

    @Test
    public void returnsCorrectWaveSizeAsDouble() {
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.MAY, 2, 0, 0);
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location XY",
                1,
                1,
                dateTime,
                "5.000"
        );
        assertEquals(surfConditions.getWaveSizeAsDouble(), Double.valueOf(5));
    }

    @Test
    public void returnsCorrectWaveSizeAsDoubleForSmallWaves() {
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.MAY, 2, 0, 0);
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location XY",
                0,
                0,
                dateTime,
                "0.001"
        );
        assertEquals(surfConditions.getWaveSizeAsDouble(), Double.valueOf(0.001));
    }

    @Test
    public void returnsCorrectLocation() {
        Location expectedLocation = new Location("Location XY", 0.5, 123.4);
        SurfConditions surfConditions = new SurfConditions(
                expectedLocation,
                LocalDateTime.of(2024, Month.MAY, 2, 0, 0),
                "2.000"
        );
        assertEquals(surfConditions.location(), expectedLocation);
    }

    @Test
    public void returnsCorrectLocationLargeCoordinates() {
        Location expectedLocation = new Location("Location Z", 179.5, 179.9);
        SurfConditions surfConditions = new SurfConditions(
                expectedLocation,
                LocalDateTime.of(2024, Month.MAY, 10, 0, 0),
                "5.000"
        );
        assertEquals(surfConditions.location(), expectedLocation);
    }

}
