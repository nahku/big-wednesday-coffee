package com.oocode;

import java.util.Locale;

public class SurfConditionsHtmlGenerator {

    private static final String waveSizeOutputTemplate = "You should have been at %s on %s - it was gnarly - waves up to %sm!";
    private static final String locationGoogleMapsLinkTemplate = "<a target=\"_blank\" href=\"http://www.google.com/maps/place/%.5f,%.5f/@%.5f,%.5f,12z\">%s</a>";

    public static String generateHtmlFromSurfConditions(SurfConditions surfConditions) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append(generateWaveSizeOutput(surfConditions));
        html.append("</body></html>");
        return html.toString();
    }

    private static String generateWaveSizeOutput(SurfConditions surfConditions) {
        return String.format(
                waveSizeOutputTemplate,
                generateGoogleMapsLinkFromLocation(surfConditions.location()),
                surfConditions.getDayOfWeek(),
                surfConditions.waveSize()
        );
    }

    private static String generateGoogleMapsLinkFromLocation(Location location) {
        return String.format(
                Locale.ENGLISH,
                locationGoogleMapsLinkTemplate,
                location.latitude(),
                location.longitude(),
                location.latitude(),
                location.longitude(),
                location.name()
        );
    }
}
