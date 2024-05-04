package com.oocode;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public record SurfConditions(String location, LocalDateTime date, String maxWaveSize){

    public SurfConditions {
        if (Double.valueOf(maxWaveSize) < 0) {
            throw new IllegalArgumentException("Wave size must be greater than or equal to zero.");
        }
    }

    public String getDayOfWeek() {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

}
