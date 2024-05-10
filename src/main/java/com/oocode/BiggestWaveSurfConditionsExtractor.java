package com.oocode;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class BiggestWaveSurfConditionsExtractor {

    public static SurfConditions extractSurfConditions(WaveData waveData, LocalDate date) {
        LocalDate fromDate = date.minusDays(3);
        LocalDate toDate = date.minusDays(1);

        List<SurfConditions> surfConditionsLastThreeDays = waveData.getSurfConditions(fromDate, toDate);

        if (surfConditionsLastThreeDays.isEmpty()) {
            throw new IllegalArgumentException("No surf conditions found for date: " + date);
        }

        return getSurfConditionsWithMaxWaveSize(surfConditionsLastThreeDays);
    }

    private static SurfConditions getSurfConditionsWithMaxWaveSize(List<SurfConditions> surfConditions) {
        return surfConditions.stream().max(Comparator.comparing(conditions -> conditions.getWaveSizeAsDouble())).get();
    }
}
