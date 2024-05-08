package com.oocode;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class QueenslandApiWaveDataTest {

    @Test
    public void extractsSurfConditionsFromCsvSingleLocation() {
        String waveCsvData = """
                    Wave Data provided @ 02:15hrs on 22-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Caloundra,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                    """.trim();
        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);

        List<SurfConditions> expectedSurfConditionsList = Arrays.asList(
                SurfConditionsHelper.createSurfConditions(
                        "Caloundra",
                        -26.84552,
                        153.15474,
                        LocalDateTime.of(2024, Month.APRIL, 21, 0, 0),
                        "1.150"),
                SurfConditionsHelper.createSurfConditions(
                        "Caloundra",
                        -26.84552,
                        153.15471,
                        LocalDateTime.of(2024, Month.APRIL, 21, 0, 30),
                        "1.170")
        );

        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getSurfConditions(today.minusDays(3), today.minusDays(1)), equalTo(expectedSurfConditionsList));
    }

    @Test
    public void extractsSurfConditionsFromCsvMultipleLocations() {
        String waveCsvData = """
                    Wave Data provided @ 02:15hrs on 22-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Location B,54,1713623400,2024-04-21T00:30:00,-26.84559,153.15479,1.234,2.234,10.530,4.167,24.70,88.60,-99.90,-99.90
                    """.trim();
        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);

        List<SurfConditions> expectedSurfConditionsList = Arrays.asList(
                SurfConditionsHelper.createSurfConditions(
                        "Caloundra",
                        -26.84552,
                        153.15474,
                        LocalDateTime.of(2024, Month.APRIL, 21, 0, 0),
                        "1.150"),
                SurfConditionsHelper.createSurfConditions(
                        "Location B",
                        -26.84559,
                        153.15479,
                        LocalDateTime.of(2024, Month.APRIL, 21, 0, 30),
                        "2.234")
        );

        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getSurfConditions(today.minusDays(3), today.minusDays(1)), equalTo(expectedSurfConditionsList));
    }

    @Test
    public void filtersSurfConditionsByDateSingleLocation() {
        String waveCsvData = """
                Wave Data provided @ 02:15hrs on 22-04-2024
                Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                Caloundra,54,1713564000,2024-04-20T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                Caloundra,54,1713650400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                Caloundra,54,1713650400,2024-04-22T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                Caloundra,54,1713823200,2024-04-23T08:00:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90

                """.trim();
        LocalDate today = LocalDate.of(2024, Month.APRIL, 24);

        List<SurfConditions> expectedSurfConditionsList = Arrays.asList(
                SurfConditionsHelper.createSurfConditions(
                        "Caloundra",
                        -26.84552,
                        153.15471,
                        LocalDateTime.of(2024, Month.APRIL, 23, 8, 0),
                        "1.170")
        );

        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getSurfConditions(today.minusDays(1), today.minusDays(1)), equalTo(expectedSurfConditionsList));
    }

    @Test
    public void filtersSurfConditionsByDateMultipleLocationsMultipleDays() {
        String waveCsvData = """
                Wave Data provided @ 02:15hrs on 22-04-2024
                Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                Caloundra,54,1713564000,2024-04-20T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                Location B,54,1713650400,2024-04-21T08:00:00,-26.84552,153.15471,0.605,5.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                Location C,54,1713736800,2024-04-22T08:00:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                Location d,54,1713823200,2024-04-23T08:00:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90

                """.trim();
        LocalDate today = LocalDate.of(2024, Month.APRIL, 24);

        List<SurfConditions> expectedSurfConditionsList = Arrays.asList(
                SurfConditionsHelper.createSurfConditions(
                        "Location B",
                        -26.84552,
                        153.15471,
                        LocalDateTime.of(2024, Month.APRIL, 21, 8, 0),
                        "5.170"),
                SurfConditionsHelper.createSurfConditions(
                        "Location C",
                        -26.84552,
                        153.15471,
                        LocalDateTime.of(2024, Month.APRIL, 22, 8, 0),
                        "1.170")
        );

        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getSurfConditions(today.minusDays(3), today.minusDays(2)), equalTo(expectedSurfConditionsList));
    }

    @Test
    public void returnsEmptyListIfFilterDatesAreNotInData() {
        String waveCsvData = """
                    Wave Data provided @ 02:15hrs on 22-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Caloundra,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                    """.trim();

        LocalDate oldDate = LocalDate.of(2000, Month.APRIL, 22);

        List<SurfConditions> expectedSurfConditionsList = new ArrayList<>();

        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getSurfConditions(oldDate.minusDays(3), oldDate.minusDays(1)), equalTo(expectedSurfConditionsList));
    }

    @Test
    public void throwsExceptionOnEmptyCsv() {

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new QueenslandApiWaveData(""));
        assertEquals("Failed to extract wave information from input.", exception.getMessage());
        assertEquals("Invalid CSV data: Expected at least 3 rows in the input.", exception.getCause().getMessage());
    }

    @Test
    public void throwsExceptionOnInvalidCsv() {
        String invalidWaveCsvData = """
                    Wave Data provided @ 02:15hrs on 28-04-2024
                    Site, SiteNumber
                    Caloundra, 54
                    Caloundra, 66
                    """.trim();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> new QueenslandApiWaveData(invalidWaveCsvData));
        assertEquals("Failed to extract wave information from input.", exception.getMessage());
        assertEquals("Invalid CSV data: Expected at least 8 columns in the input.", exception.getCause().getMessage());
    }

}
