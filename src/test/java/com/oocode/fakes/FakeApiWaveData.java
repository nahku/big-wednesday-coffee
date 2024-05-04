package com.oocode.fakes;

import com.oocode.ApiWaveData;
import com.oocode.WaveInfo;

/*
 Fakes an instance of the ApiWaveData interface.
 Used to test that the WaveInfoExtractor extracts the correct WaveInfo.
 */
public class FakeApiWaveData implements ApiWaveData {

    private final WaveInfo waveInfo;

    public FakeApiWaveData(WaveInfo waveInfo){
        this.waveInfo = waveInfo;
    }

    @Override
    public WaveInfo getMaxWaveInfo() {
        return waveInfo;
    }
}
