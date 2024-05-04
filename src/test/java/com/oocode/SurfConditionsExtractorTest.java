package com.oocode;

import com.oocode.fakes.FakeWaveData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SurfConditionsExtractorTest {

    @Test
    public void canExtractCorrectWaveInfo() {

        SurfConditions expectedSurfConditions = new SurfConditions(
                "Location A",
                LocalDateTime.of(2000, Month.APRIL, 2, 0, 0),
                "5.000");

        WaveData waveData = new FakeWaveData(expectedSurfConditions);

        assertThat(SurfConditionsExtractor.extractSurfConditions(waveData), equalTo(expectedSurfConditions));
    }

    @Test
    public void canExtractCorrectWaveInfoDifferentLocation() {

        SurfConditions expectedSurfConditions = new SurfConditions(
                "Location A",
                LocalDateTime.of(2024, Month.APRIL, 2, 0, 0),
                "7.000");

        WaveData waveData = new FakeWaveData(expectedSurfConditions);

        assertThat(SurfConditionsExtractor.extractSurfConditions(waveData), equalTo(expectedSurfConditions));
    }
}
