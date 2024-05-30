package com.oocode.fakes;

import com.oocode.WaveData;
import com.oocode.SurfConditions;

import java.time.LocalDate;
import java.util.List;

/*
 Fakes an instance of the ApiWaveData interface and returns the SurfConditions it was instantiated with.
 Does not filter by from and toDate as this is not required to test the BiggestWaveSurfConditionsExtractor.
 */
public class FakeWaveData implements WaveData {

    private final List<SurfConditions> surfConditions;

    public LocalDate fromDateCalled; // To check whether the getSurfConditions function was called with the correct dates.
    public LocalDate toDateCalled;

    public FakeWaveData(List<SurfConditions> surfConditions){
        this.surfConditions = surfConditions;
    }

    @Override
    public List<SurfConditions> getSurfConditions(LocalDate fromDate, LocalDate toDate) {
        fromDateCalled = fromDate;
        toDateCalled = toDate;
        return surfConditions;
    }
}
