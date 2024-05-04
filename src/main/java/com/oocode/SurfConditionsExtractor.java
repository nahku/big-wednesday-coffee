package com.oocode;

public class SurfConditionsExtractor {

    public static SurfConditions extractSurfConditions(WaveData waveData) {
        return waveData.getLargestWaveSurfConditions();
    }
}
