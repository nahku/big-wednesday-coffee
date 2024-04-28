package com.oocode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import io.fusionauth.http.server.HTTPHandler;
import io.fusionauth.http.server.HTTPListenerConfiguration;
import io.fusionauth.http.server.HTTPServer;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class E2eTests {

    private HTTPServer server;

    @Test
    public void canFindMaximumWaveHeightFromQueenslandDataSingleLocation() throws Exception {
        startLocalServerPretendingToBeQueenslandApi("""
                    Wave Data provided @ 02:15hrs on 28-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Caloundra,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                    Caloundra,54,1713625200,2024-04-21T01:00:00,-26.84553,153.15469,0.624,1.150,10.530,4.167,24.70,91.40,-99.90,-99.90
                    Caloundra,54,1713627000,2024-04-21T01:30:00,-26.84553,153.15459,0.660,1.140,10.000,4.348,24.70,94.20,-99.90,-99.90
                    """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 28);

        String expectedOutput = "<html><body>You should have been at Caloundra on Sunday - it was gnarly - waves up to 1.170m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void canFindMaximumWaveHeightFromQueenslandDataMultipleLocations() throws Exception {
        startLocalServerPretendingToBeQueenslandApi("""
                    Wave Data provided @ 02:15hrs on 28-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Location B,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.300,10.530,4.167,24.70,88.60,-99.90,-99.90
                    """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 28);

        String expectedOutput = "<html><body>You should have been at Location B on Sunday - it was gnarly - waves up to 1.300m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void canFindMaximumWaveHeightFromQueenslandDataSimilarHeight() throws Exception {
        // Tests if correct height is returned if height difference is small
        startLocalServerPretendingToBeQueenslandApi("""
                    Wave Data provided @ 02:15hrs on 28-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.171,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Caloundra,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                    Caloundra,54,1713625200,2024-04-21T01:00:00,-26.84553,153.15469,0.624,1.169,10.530,4.167,24.70,91.40,-99.90,-99.90
                    Caloundra,54,1713627000,2024-04-21T01:30:00,-26.84553,153.15459,0.660,1.170,10.000,4.348,24.70,94.20,-99.90,-99.90
                    """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 28);

        String expectedOutput = "<html><body>You should have been at Caloundra on Sunday - it was gnarly - waves up to 1.171m!</body></html>";
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

        startLocalServerPretendingToBeQueenslandApi("""
                    Wave Data provided @ 02:15hrs on 28-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Location D,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.9,1.2,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Location A,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.9,1.2,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Location B,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.9,1.2,10.530,4.167,24.70,88.60,-99.90,-99.90
                    Location C,54,1713625200,2024-04-21T01:00:00,-26.84553,153.15469,0.9,1.2,10.530,4.167,24.70,91.40,-99.90,-99.90
                    """.trim());

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 28);

        String expectedOutput = "<html><body>You should have been at Location D on Sunday - it was gnarly - waves up to 1.2m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @Test
    public void canFindMaximumWaveHeightFromOriginalQueenslandData() throws Exception {
        String input = Files.readString(Paths.get("src/test/resources/queensland_test_data.csv"));

        startLocalServerPretendingToBeQueenslandApi(input);

        String url = "http://localhost:8123";
        LocalDate date = LocalDate.of(2024, Month.APRIL, 28);

        String expectedOutput = "<html><body>You should have been at Wide Bay on Monday - it was gnarly - waves up to 7.660m!</body></html>";
        Main.main(new String[]{url, date.toString()});

        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        assertThat(output, equalTo(expectedOutput));
    }

    @BeforeEach
    public void deletePreviousOutputIfItExists() {
        // Deletes the (potential) previous output to have a clean test
        Path path = Paths.get("index.html");
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not delete old index.html.", e);
            }
        }
    }

    public void startLocalServerPretendingToBeQueenslandApi(String response) {
        HTTPHandler handler = (req, res) -> {
            try (Writer writer = res.getWriter()) {
                writer.write(response);
            }
        };

        server = new HTTPServer().withHandler(handler).withListener(new HTTPListenerConfiguration(8123));
        server.start();
    }

    @AfterEach
    public void stopLocalServerPretendingToBeNationalGridEso() {
        server.close();
    }
}