package com.oocode;

import com.oocode.utils.SurfConditionsHelper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class SurfConditionsHtmlGeneratorTest {

    @Test
    public void returnsHtmlWithBody() {
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location XY",
                0,
                0,
                LocalDateTime.of(2024, Month.MAY, 1, 0, 30),
                "2.000"
        );

        String response = SurfConditionsHtmlGenerator.generateHtmlFromSurfConditions(surfConditions);

        assertTrue(response.startsWith("<html><body>"));
        assertTrue(response.endsWith("</body></html>"));
    }

    @Test
    public void returnsHtmlWaveInfoForWaveDataInput() {
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location A",
                0,
                0,
                LocalDateTime.of(2024, Month.MAY, 1, 0, 0),
                "5.000"
        );

        String expectedResponseStart = "<html><body>You should have been at ";
        String expectedResponseEnd = " on Wednesday - it was gnarly - waves up to 5.000m!</body></html>";

        String response = SurfConditionsHtmlGenerator.generateHtmlFromSurfConditions(surfConditions);

        assertTrue(response.startsWith(expectedResponseStart));
        assertTrue(response.contains("Location A"));
        assertTrue(response.endsWith(expectedResponseEnd));
    }

    @Test
    public void returnsFormattedHtmlWaveInfoForWaveDataInputDifferentDate() {
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location C",
                2.3,
                4.8,
                LocalDateTime.of(2020, Month.APRIL, 6, 20, 30),
                "10.000"
        );

        String expectedResponseStart = "<html><body>You should have been at ";
        String expectedResponseEnd = " on Monday - it was gnarly - waves up to 10.000m!</body></html>";

        String response = SurfConditionsHtmlGenerator.generateHtmlFromSurfConditions(surfConditions);

        assertTrue(response.startsWith(expectedResponseStart));
        assertTrue(response.contains("Location C"));
        assertTrue(response.endsWith(expectedResponseEnd));
    }

    @Test
    public void returnsFormattedHtmlForWaveWithGoogleMapsLink() {
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Location C",
                -2.3,
                4.8,
                LocalDateTime.of(2020, Month.APRIL, 6, 20, 30),
                "10.000"
        );

        String expectedResult = "<html><body>You should have been at " +
                "<a target=\"_blank\" href=\"http://www.google.com/maps/place/-2.30000,4.80000/@-2.30000,4.80000,12z\">Location C</a> " +
                "on Monday - it was gnarly - waves up to 10.000m!</body></html>";

        String response = SurfConditionsHtmlGenerator.generateHtmlFromSurfConditions(surfConditions);

        assertThat(response, equalTo(expectedResult));
    }

    @Test
    public void returnsFormattedHtmlForWaveWithGoogleMapsLinkDifferentLocation() {
        SurfConditions surfConditions = SurfConditionsHelper.createSurfConditions(
                "Caloundra",
                -122.3,
                46.8,
                LocalDateTime.of(2020, Month.APRIL, 6, 20, 30),
                "10.000"
        );

        String expectedResult = "<html><body>You should have been at " +
                "<a target=\"_blank\" href=\"http://www.google.com/maps/place/-122.30000,46.80000/@-122.30000,46.80000,12z\">Caloundra</a> " +
                "on Monday - it was gnarly - waves up to 10.000m!</body></html>";

        String response = SurfConditionsHtmlGenerator.generateHtmlFromSurfConditions(surfConditions);

        assertThat(response, equalTo(expectedResult));
    }

}
