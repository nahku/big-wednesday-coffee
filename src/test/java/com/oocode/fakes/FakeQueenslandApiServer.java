package com.oocode.fakes;

import io.fusionauth.http.server.HTTPHandler;
import io.fusionauth.http.server.HTTPListenerConfiguration;
import io.fusionauth.http.server.HTTPServer;

import java.io.Writer;

public class FakeQueenslandApiServer {

    private HTTPServer server;

    public void startLocalServerPretendingToBeQueenslandApi(String response) {
        HTTPHandler handler = (req, res) -> {
            try (Writer writer = res.getWriter()) {
                writer.write(response);
            }
        };

        server = new HTTPServer().withHandler(handler).withListener(new HTTPListenerConfiguration(8123));
        server.start();
    }

    public void startLocalServerPretendingToBeQueenslandApiReturningAnInternalError() {
        HTTPHandler handler = (req, res) -> res.setStatus(500);

        server = new HTTPServer().withHandler(handler).withListener(new HTTPListenerConfiguration(8123));
        server.start();
    }

    public void stopLocalServerPretendingToBeQueenslandApi() {
        server.close();
    }

}
