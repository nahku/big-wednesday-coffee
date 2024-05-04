package com.oocode;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public record WaveInfo(String location, LocalDateTime date, String maxWaveSize){

    public WaveInfo {
        if (Double.valueOf(maxWaveSize) < 0) {
            throw new IllegalArgumentException("Wave size must be greater than or equal to zero.");
        }
    }

    public String getDayOfWeek() {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

}
