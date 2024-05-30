package com.oocode;

public class SurfConditionsHtmlGenerator {

    public static String generateHtmlFromSurfConditions(SurfConditions surfConditions) {

        String dayOfWeek = surfConditions.getDayOfWeek();

        return String.format(
                    "<html><body>You should have been at %s on %s - it was gnarly - waves up to %sm!</body></html>",
                    surfConditions.location(),
                    dayOfWeek,
                    surfConditions.waveSize());
    }
}
