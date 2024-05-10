package com.oocode;

import com.oocode.fakes.FakeQueenslandApiServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class E2eTest {

    FakeQueenslandApiServer server;

    @Test
    public void canFindMaximumWaveHeightFromQueenslandDataSingleLocation() throws Exception {
        server.startLocalServerPretendingToBeQueenslandApi("""
                Wave Data provided @ 02:15hrs on 28-04-2024
                Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                Caloundra,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                Caloundra,54,1713625200,2024-04-21T01:00:00,-26.84553,153.15469,0.624,1.150,10.530,4.167,24.70,91.40,-99.90,-99.90
                Caloundra,54,1713627000,2024-04-21T01:30:00,-26.84553,153.15459,0.660,1.140,10.000,4.348,24.70,94.20,-99.90,-99.90
                """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 22);

        String expectedOutput = "<html><body>You should have been at " +
                "<a target=\"_blank\" href=\"http://www.google.com/maps/place/-26.84552,153.15471/@-26.84552,153.15471,12z\">Caloundra</a> " +
                "on Sunday - it was gnarly - waves up to 1.170m!</body></html>";

        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void canFindMaximumWaveHeightFromQueenslandDataMultipleLocations() throws Exception {
        server.startLocalServerPretendingToBeQueenslandApi("""
                Wave Data provided @ 02:15hrs on 28-04-2024
                Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                Location B,54,1713623400,2024-04-21T00:30:00,-26.84532,153.15641,0.605,1.300,10.530,4.167,24.70,88.60,-99.90,-99.90
                """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 22);

        String expectedOutput = "<html><body>You should have been at " +
                "<a target=\"_blank\" href=\"http://www.google.com/maps/place/-26.84532,153.15641/@-26.84532,153.15641,12z\">Location B</a> " +
                "on Sunday - it was gnarly - waves up to 1.300m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void canFindMaximumWaveHeightFromQueenslandDataSimilarHeight() throws Exception {
        // Tests if correct height is returned if height difference is small
        server.startLocalServerPretendingToBeQueenslandApi("""
                Wave Data provided @ 02:15hrs on 28-04-2024
                Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.171,10.530,4.040,24.70,75.90,-99.90,-99.90
                Caloundra,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                Caloundra,54,1713625200,2024-04-21T01:00:00,-26.84553,153.15469,0.624,1.169,10.530,4.167,24.70,91.40,-99.90,-99.90
                Caloundra,54,1713627000,2024-04-21T01:30:00,-26.84553,153.15459,0.660,1.170,10.000,4.348,24.70,94.20,-99.90,-99.90
                """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 22);

        String expectedOutput = "<html><body>You should have been at " +
                "<a target=\"_blank\" href=\"http://www.google.com/maps/place/-26.84552,153.15474/@-26.84552,153.15474,12z\">Caloundra</a>" +
                " on Sunday - it was gnarly - waves up to 1.171m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void canFindMaximumWaveHeightFromQueenslandDataSameHeight() throws Exception {
        /*
        Tests the behaviour if multiple locations have the same maximum wave height.
        Serves also as documentation that in that case the first location is returned.
         */

        server.startLocalServerPretendingToBeQueenslandApi("""
                Wave Data provided @ 02:15hrs on 28-04-2024
                Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                Location D,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.9,1.200,10.530,4.040,24.70,75.90,-99.90,-99.90
                Location A,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.9,1.200,10.530,4.040,24.70,75.90,-99.90,-99.90
                Location B,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.9,1.200,10.530,4.167,24.70,88.60,-99.90,-99.90
                Location C,54,1713625200,2024-04-21T01:00:00,-26.84553,153.15469,0.9,1.200,10.530,4.167,24.70,91.40,-99.90,-99.90
                """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 23);

        String expectedOutput = "<html><body>You should have been at " +
                "<a target=\"_blank\" href=\"http://www.google.com/maps/place/-26.84552,153.15474/@-26.84552,153.15474,12z\">Location D</a> " +
                "on Sunday - it was gnarly - waves up to 1.200m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void canFindMaximumWaveHeightFromOriginalQueenslandData() throws Exception {
        String input = Files.readString(Paths.get("src/test/resources/queensland_test_data.csv"));

        server.startLocalServerPretendingToBeQueenslandApi(input);

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 22);

        String expectedOutput = "<html><body>You should have been at " +
                "<a target=\"_blank\" href=\"http://www.google.com/maps/place/-25.75825,153.20520/@-25.75825,153.20520,12z\">Wide Bay</a> " +
                "on Sunday - it was gnarly - waves up to 6.880m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void canFindMaximumWaveHeightPreviousThreeDays() throws Exception {

        server.startLocalServerPretendingToBeQueenslandApi("""
                Wave Data provided @ 02:15hrs on 28-04-2024
                Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                Location D,54,1713564000,2024-04-20T00:00:00,-26.84552,153.15474,0.9,12.200,10.530,4.040,24.70,75.90,-99.90,-99.90
                Location A,54,1713650400,2024-04-21T00:00:00,-26.84552,153.15474,0.9,1.200,10.530,4.040,24.70,75.90,-99.90,-99.90
                Location B,54,1713736800,2024-04-22T00:00:00,-26.84552,153.15471,0.9,1.300,10.530,4.167,24.70,88.60,-99.90,-99.90
                Location C,54,1713823200,2024-04-23T01:00:00,-26.84553,153.15469,0.9,1.400,10.530,4.167,24.70,91.40,-99.90,-99.90
                """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 24);

        String expectedOutput = "<html><body>You should have been at " +
                "<a target=\"_blank\" href=\"http://www.google.com/maps/place/-26.84553,153.15469/@-26.84553,153.15469,12z\">Location C</a> " +
                "on Tuesday - it was gnarly - waves up to 1.400m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void throwsExceptionOnInvalidInputTooFew() {
        server.startLocalServerPretendingToBeQueenslandApi("");
        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"http://localhost:8123"}));
        assertEquals("Either provide url and current date or no arguments.", exception.getMessage());
    }

    @Test
    public void throwsExceptionOnInvalidInputTooManyArguments() {
        server.startLocalServerPretendingToBeQueenslandApi("");
        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"http://localhost:8123", "input 2", "input 3"}));
        assertEquals("Either provide url and current date or no arguments.", exception.getMessage());
    }

    @Test
    public void throwsExceptionOnInvalidInputInvalidDate() {
        server.startLocalServerPretendingToBeQueenslandApi("");
        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"http://localhost:8123", "This should be a date."}));
        assertEquals("Provided date could not be parsed.", exception.getMessage());
    }

    @Test
    public void throwsExceptionOnInvalidInputCurrentDateOutOfDataRange() {

        server.startLocalServerPretendingToBeQueenslandApi("""
                Wave Data provided @ 02:15hrs on 28-04-2024
                Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                Location D,54,1713564000,2024-04-20T00:00:00,-26.84552,153.15474,0.9,12.200,10.530,4.040,24.70,75.90,-99.90,-99.90 
                """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2025, Month.APRIL, 24);

        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{url, date.toString()}));
        assertEquals("No surf conditions found for date: 2025-04-24", exception.getMessage());
    }

    @Test
    public void throwsExceptionOnInvalidInputInvalidUrl() {
        server.startLocalServerPretendingToBeQueenslandApi("");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"http://This should be a url.", "2024-05-05"}));
        assertEquals("Invalid URL host: \"This should be a url.\"", exception.getMessage());
    }

    @BeforeEach
    @AfterEach
    public void deleteOutputIfItExists() {
        FileHandlingHelper.deleteFileIfItExists("index.html");
    }

    @BeforeEach
    public void setServer() {
        server = new FakeQueenslandApiServer();
    }

    @AfterEach
    public void stopLocalServerPretendingToBeNationalGridEso() {
        server.stopLocalServerPretendingToBeQueenslandApi();
    }

}