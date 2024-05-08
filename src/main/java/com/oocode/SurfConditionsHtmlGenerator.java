package com.oocode;

public class SurfConditionsHtmlGenerator {

    public static String generateHtmlFromSurfConditions(SurfConditions surfConditions) {

        return String.format(
                    "<html><body>You should have been at %s on %s - it was gnarly - waves up to %sm!</body></html>",
                    surfConditions.location().name(),
                    surfConditions.getDayOfWeek(),
                    surfConditions.waveSize());
    }
}
