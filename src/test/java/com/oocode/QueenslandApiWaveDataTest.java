package com.oocode;

import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class QueenslandApiWaveDataTest {

    @Test
    public void extractsWaveInfoFromCsv() {
        String waveCsvData = """
                    Wave Data provided @ 02:15hrs on 22-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Caloundra,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                    Caloundra,54,1713625200,2024-04-21T01:00:00,-26.84553,153.15469,0.624,1.150,10.530,4.167,24.70,91.40,-99.90,-99.90
                    Caloundra,54,1713627000,2024-04-21T01:30:00,-26.84553,153.15459,0.660,1.140,10.000,4.348,24.70,94.20,-99.90,-99.90
                    """.trim();
        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);

        SurfConditions expectedSurfConditions = new SurfConditions("Caloundra", LocalDateTime.of(2024, Month.APRIL, 21, 0, 30), "1.170");
        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getLargestWaveSurfConditions(today), equalTo(expectedSurfConditions));
    }

    @Test
    public void extractsWaveInfoFromCsvMultipleLocations() {
        String waveCsvData = """
                    Wave Data provided @ 02:15hrs on 22-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Location B,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,1.234,2.234,10.530,4.167,24.70,88.60,-99.90,-99.90
                    """.trim();
        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);

        SurfConditions expectedSurfConditions = new SurfConditions("Location B", LocalDateTime.of(2024, Month.APRIL, 21, 0, 30), "2.234");

        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getLargestWaveSurfConditions(today), equalTo(expectedSurfConditions));
    }

    @Test
    public void extractsWaveInfoFromCsvSimilarHeight() {
        String waveCsvData = """
                    Wave Data provided @ 02:15hrs on 22-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Location B,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.645,1.149,10.530,4.167,24.70,88.60,-99.90,-99.90
                    """.trim();
        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);

        SurfConditions expectedSurfConditions = new SurfConditions("Caloundra", LocalDateTime.of(2024, Month.APRIL, 21, 0, 0), "1.150");

        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getLargestWaveSurfConditions(today), equalTo(expectedSurfConditions));
    }

    @Test
    @Disabled
    public void extractsWaveInfoFromCsvPreviousThreeDays() {
        String waveCsvData = """
                    Wave Data provided @ 02:15hrs on 22-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Caloundra,54,1713623400,2024-04-20T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                    Caloundra,54,1713625200,2024-04-19T01:00:00,-26.84553,153.15469,0.624,1.150,10.530,4.167,24.70,91.40,-99.90,-99.90
                    """.trim();
        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);
        SurfConditions expectedSurfConditions = new SurfConditions("Caloundra", LocalDateTime.of(2024, Month.APRIL, 22, 0, 30), "1.170");

        QueenslandApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);

        assertThat(waveData.getLargestWaveSurfConditions(today), equalTo(expectedSurfConditions));
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
