package com.oocode;

public class WaveInfoExtractor {

    public static WaveInfo extractWaveInfo(ApiWaveData waveData) {
        return waveData.getMaxWaveInfo();
    }
}
