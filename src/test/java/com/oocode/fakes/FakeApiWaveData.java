package com.oocode.fakes;

import com.oocode.ApiWaveData;
import com.oocode.SurfConditions;

/*
 Fakes an instance of the ApiWaveData interface.
 Used to test that the WaveInfoExtractor extracts the correct WaveInfo.
 */
public class FakeApiWaveData implements ApiWaveData {

    private final SurfConditions surfConditions;

    public FakeApiWaveData(SurfConditions surfConditions){
        this.surfConditions = surfConditions;
    }

    @Override
    public SurfConditions getMaxWaveInfo() {
        return surfConditions;
    }
}
