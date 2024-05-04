package com.oocode;

public class SurfConditionsExtractor {

    public static SurfConditions extractWaveInfo(WaveData waveData) {
        return waveData.getLargestWaveSurfConditions();
    }
}
