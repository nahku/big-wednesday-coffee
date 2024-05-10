package com.oocode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public record SurfConditions(Location location, LocalDateTime date, String waveSize) {

    public SurfConditions {
        if (Double.valueOf(waveSize) < 0) {
            throw new IllegalArgumentException("Wave size must be greater than or equal to zero.");
        }
    }

    public String getDayOfWeek() {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public LocalDate getDate() {
        return LocalDate.from(date);
    }

    public Double getWaveSizeAsDouble() {
        return Double.valueOf(waveSize);
    }

}
