package com.oocode;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class HtmlGeneratorTest {

    @Test
    public void returnsFormattedHtmlForWaveDataInput() {
        WaveInfo waveInfo = new WaveInfo("Location A", LocalDateTime.of(2024, Month.MAY,1, 0,0), "5.000");

        String expectedResult = "<html><body>You should have been at Location A on Wednesday - it was gnarly - waves up to 5.000m!</body></html>";

        String response = HtmlGenerator.generateHtmlFromWaveInfo(waveInfo);

        assertThat(response, equalTo(expectedResult));
    }

    @Test
    public void returnsFormattedHtmlForWaveDataInputDifferentDate() {
        WaveInfo waveInfo = new WaveInfo("Location C", LocalDateTime.of(2020, Month.APRIL,6, 20,30), "10.000");

        String expectedResult = "<html><body>You should have been at Location C on Monday - it was gnarly - waves up to 10.000m!</body></html>";

        String response = HtmlGenerator.generateHtmlFromWaveInfo(waveInfo);

        assertThat(response, equalTo(expectedResult));
    }

}
