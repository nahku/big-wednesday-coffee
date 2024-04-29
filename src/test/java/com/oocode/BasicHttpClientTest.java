package com.oocode;

import com.oocode.fakes.FakeQueenslandApiServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BasicHttpClientTest {

    FakeQueenslandApiServer server;

    @Test
    public void returnsResponseAsString() {
        String expectedResponse = "Response of the server.";

        server.startLocalServerPretendingToBeQueenslandApi(expectedResponse);

        BasicHttpClient client = new BasicHttpClient();
        String response = client.readUrl("http://localhost:8123");

        assertThat(response, equalTo(expectedResponse));
    }

    @Test
    public void returnsCsvResponseAsString() {
        String expectedResponse = """
                    Wave Data provided @ 02:15hrs on 28-04-2024
                    Site, SiteNumber, Seconds, DateTime, Latitude, Longitude, Hsig, Hmax, Tp, Tz, SST, Direction, Current Speed, Current Direction
                    Caloundra,54,1713621600,2024-04-21T00:00:00,-26.84552,153.15474,0.646,1.150,10.530,4.040,24.70,75.90,-99.90,-99.90
                    Caloundra,54,1713623400,2024-04-21T00:30:00,-26.84552,153.15471,0.605,1.170,10.530,4.167,24.70,88.60,-99.90,-99.90
                    Caloundra,54,1713625200,2024-04-21T01:00:00,-26.84553,153.15469,0.624,1.150,10.530,4.167,24.70,91.40,-99.90,-99.90
                    Caloundra,54,1713627000,2024-04-21T01:30:00,-26.84553,153.15459,0.660,1.140,10.000,4.348,24.70,94.20,-99.90,-99.90
                    """.trim();
        server.startLocalServerPretendingToBeQueenslandApi(expectedResponse);

        BasicHttpClient client = new BasicHttpClient();
        String response = client.readUrl("http://localhost:8123");

        assertThat(response, equalTo(expectedResponse));
    }

    @Test
    public void throwsExceptionOnInvalidUrl() {
        server.startLocalServerPretendingToBeQueenslandApi("Response of the server.");

        String imaginaryUrl = "http://imaginary_url:8123";

        BasicHttpClient client = new BasicHttpClient();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> client.readUrl(imaginaryUrl));
        assertEquals("Reading url failed: " + imaginaryUrl, exception.getMessage());

    }

    @Test
    public void throwsExceptionOnServerError() {
        server.startLocalServerPretendingToBeQueenslandApiReturningAnInternalError();

        BasicHttpClient client = new BasicHttpClient();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> client.readUrl("http://localhost:8123"));
        assertEquals("Reading url failed: http://localhost:8123. Error code: 500", exception.getMessage());

    }

    @BeforeEach
    public void setServer() {
        server = new FakeQueenslandApiServer();
    }

    @AfterEach
    public void stopServer() {
        server.stopLocalServerPretendingToBeQueenslandApi();
    }
}
