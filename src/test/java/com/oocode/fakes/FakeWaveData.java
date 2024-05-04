package com.oocode.fakes;

import com.oocode.WaveData;
import com.oocode.SurfConditions;

import java.time.LocalDate;

/*
 Fakes an instance of the ApiWaveData interface.
 Used to test that the WaveInfoExtractor extracts the correct WaveInfo.
 */
public class FakeWaveData implements WaveData {

    private final SurfConditions surfConditions;

    public FakeWaveData(SurfConditions surfConditions){
        this.surfConditions = surfConditions;
    }

    @Override
    public SurfConditions getLargestWaveSurfConditions(LocalDate date) {
        return surfConditions;
    }
}
