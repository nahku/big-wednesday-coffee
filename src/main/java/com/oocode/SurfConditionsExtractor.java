package com.oocode;

import java.time.LocalDate;

public class SurfConditionsExtractor {

    public static SurfConditions extractSurfConditions(WaveData waveData, LocalDate date) {
        return waveData.getLargestWaveSurfConditions(date);
    }
}
