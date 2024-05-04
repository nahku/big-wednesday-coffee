package com.oocode;

import com.oocode.fakes.FakeApiWaveData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class WaveInfoExtractorTest {

    @Test
    public void canExtractCorrectWaveInfo() {

        WaveInfo expectedWaveInfo = new WaveInfo(
                "Location A",
                LocalDateTime.of(2000, Month.APRIL, 2, 0, 0),
                "5.000");

        ApiWaveData waveData = new FakeApiWaveData(expectedWaveInfo);

        assertThat(WaveInfoExtractor.extractWaveInfo(waveData), equalTo(expectedWaveInfo));
    }

    @Test
    public void canExtractCorrectWaveInfoDifferentLocation() {

        WaveInfo expectedWaveInfo = new WaveInfo(
                "Location A",
                LocalDateTime.of(2024, Month.APRIL, 2, 0, 0),
                "7.000");

        ApiWaveData waveData = new FakeApiWaveData(expectedWaveInfo);

        assertThat(WaveInfoExtractor.extractWaveInfo(waveData), equalTo(expectedWaveInfo));
    }
}
