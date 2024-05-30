package com.oocode;

import com.oocode.fakes.FakeWaveData;
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

public class BiggestWaveSurfConditionsExtractorTest {

    @Test
    public void canExtractCorrectSurfConditions() {

        SurfConditions expectedSurfConditions = new SurfConditions("Location A",
                LocalDateTime.of(2000, Month.APRIL, 2, 1, 0),
                "6.000");

        List<SurfConditions> surfConditionsList = Arrays.asList(
                new SurfConditions("Location A",
                        LocalDateTime.of(2000, Month.APRIL, 2, 0, 0),
                        "5.000"),
                expectedSurfConditions
        );

        LocalDate date = LocalDate.of(2000, Month.APRIL, 3);
        WaveData waveData = new FakeWaveData(surfConditionsList);

        assertThat(BiggestWaveSurfConditionsExtractor.extractSurfConditions(waveData, date), equalTo(expectedSurfConditions));
    }

    @Test
    public void canExtractCorrectSurfConditionsMultipleLocations() {

        SurfConditions expectedSurfConditions = new SurfConditions("Location A",
                LocalDateTime.of(2000, Month.APRIL, 2, 1, 0),
                "6.000");

        List<SurfConditions> surfConditionsList = Arrays.asList(
                new SurfConditions("Location B",
                        LocalDateTime.of(2000, Month.APRIL, 2, 0, 0),
                        "5.000"),
                new SurfConditions("Location C",
                        LocalDateTime.of(2000, Month.APRIL, 2, 2, 0),
                        "5.900"),
                expectedSurfConditions
        );

        LocalDate date = LocalDate.of(2000, Month.APRIL, 3);
        WaveData waveData = new FakeWaveData(surfConditionsList);

        assertThat(BiggestWaveSurfConditionsExtractor.extractSurfConditions(waveData, date), equalTo(expectedSurfConditions));
    }

    @Test
    public void extractsSurfConditionsFromCsvSimilarHeight() {

        SurfConditions expectedSurfConditions = new SurfConditions("Caloundra",
                LocalDateTime.of(2024, Month.APRIL, 21, 0, 0),
                "5.000");

        List<SurfConditions> surfConditionsList = Arrays.asList(
                new SurfConditions("Location B",
                        LocalDateTime.of(2024, Month.APRIL, 21, 0, 30),
                        "4.900"),
                expectedSurfConditions
        );

        WaveData waveData = new FakeWaveData(surfConditionsList);

        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);

        assertThat(BiggestWaveSurfConditionsExtractor.extractSurfConditions(waveData, today), equalTo(expectedSurfConditions));
    }

    @Test
    public void extractsSurfConditionsFromCsvPreviousThreeDays() {
        // Tests that the extractor calls the data representation with the correct dates

        SurfConditions expectedSurfConditions = new SurfConditions("Caloundra",
                LocalDateTime.of(2024, Month.APRIL, 21, 0, 0),
                "0.646");

        List<SurfConditions> surfConditionsList = Arrays.asList(
                new SurfConditions("Caloundra",
                        LocalDateTime.of(2024, Month.APRIL, 20, 0, 30),
                        "0.605"),
                new SurfConditions("Caloundra",
                        LocalDateTime.of(2024, Month.APRIL, 20, 1, 00),
                        "0.624"),
                expectedSurfConditions
        );

        FakeWaveData waveData = new FakeWaveData(surfConditionsList);

        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);

        assertThat(BiggestWaveSurfConditionsExtractor.extractSurfConditions(waveData, today), equalTo(expectedSurfConditions));

        assertThat(waveData.fromDateCalled, equalTo(LocalDate.of(2024, Month.APRIL, 19)));
        assertThat(waveData.toDateCalled, equalTo(LocalDate.of(2024, Month.APRIL, 21)));
    }

    @Test
    public void throwsExceptionIfNoDataIsAvailable() {
        FakeWaveData waveData = new FakeWaveData(new ArrayList<>());

        LocalDate today = LocalDate.of(2024, Month.APRIL, 20);

        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> BiggestWaveSurfConditionsExtractor.extractSurfConditions(waveData, today));
        assertEquals("No surf conditions found for date: 2024-04-20", exception.getMessage());
    }
}
