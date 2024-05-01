package com.oocode;

import java.time.format.TextStyle;
import java.util.Locale;

public class WaveInfoHtmlGenerator {

    public static String generateHtmlFromWaveInfo(WaveInfo waveInfo) {

        String dateFormatted = waveInfo.date().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return String.format(
                    "<html><body>You should have been at %s on %s - it was gnarly - waves up to %sm!</body></html>",
                    waveInfo.location(),
                    dateFormatted,
                    waveInfo.maxWaveSize());
    }
}
