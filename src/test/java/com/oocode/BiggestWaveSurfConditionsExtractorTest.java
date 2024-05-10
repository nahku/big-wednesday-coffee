package com.oocode;

import com.oocode.fakes.FakeWaveData;
import com.oocode.utils.SurfConditionsHelper;
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

        SurfConditions expectedSurfConditions = SurfConditionsHelper.createSurfConditions(
                "Location A",
                1.0,
                3.0,
                LocalDateTime.of(2000, Month.APRIL, 2, 1, 0),
                "6.000");

        List<SurfConditions> surfConditionsList = Arrays.asList(
                SurfConditionsHelper.createSurfConditions(
                        "Location A",
                        3.45,
                        4.56,
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

        SurfConditions expectedSurfConditions = SurfConditionsHelper.createSurfConditions(
                "Location A",
                2.34,
                3.45,
                LocalDateTime.of(2000, Month.APRIL, 2, 1, 0),
                "6.000"
        );

        List<SurfConditions> surfConditionsList = Arrays.asList(
                SurfConditionsHelper.createSurfConditions(
                        "Location B",
                        3.34,
                        6.54,
                        LocalDateTime.of(2000, Month.APRIL, 2, 0, 0),
                        "5.000"),
                SurfConditionsHelper.createSurfConditions(
                        "Location C",
                        45.3,
                        44.3,
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

        SurfConditions expectedSurfConditions = SurfConditionsHelper.createSurfConditions(
                "Caloundra",
                43.53,
                54.34,
                LocalDateTime.of(2024, Month.APRIL, 21, 0, 0),
                "5.000"
        );

        List<SurfConditions> surfConditionsList = Arrays.asList(
                SurfConditionsHelper.createSurfConditions(
                        "Location B",
                        0,
                        0,
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
        SurfConditions expectedSurfConditions = SurfConditionsHelper.createSurfConditions(
                "Caloundra",
                0,
                0,
                LocalDateTime.of(2024, Month.APRIL, 21, 0, 0),
                "0.646");

        List<SurfConditions> surfConditionsList = Arrays.asList(
                SurfConditionsHelper.createSurfConditions(
                        "Caloundra",
                        0,
                        0,
                        LocalDateTime.of(2024, Month.APRIL, 20, 0, 30),
                        "0.605"),
                SurfConditionsHelper.createSurfConditions(
                        "Caloundra",
                        0,
                        0,
                        LocalDateTime.of(2024, Month.APRIL, 20, 1, 00),
                        "0.624"),
                expectedSurfConditions
        );

        FakeWaveData waveData = new FakeWaveData(surfConditionsList);

        LocalDate today = LocalDate.of(2024, Month.APRIL, 22);

        assertThat(BiggestWaveSurfConditionsExtractor.extractSurfConditions(waveData, today), equalTo(expectedSurfConditions));
    }

    @Test
    public void throwsExceptionIfNoDataIsAvailable() {
        FakeWaveData waveData = new FakeWaveData(new ArrayList<>());

        LocalDate today = LocalDate.of(2024, Month.APRIL, 20);

        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> BiggestWaveSurfConditionsExtractor.extractSurfConditions(waveData, today));
        assertEquals("No surf conditions found for date: 2024-04-20", exception.getMessage());
    }
}
