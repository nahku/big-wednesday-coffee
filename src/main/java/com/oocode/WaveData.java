package com.oocode;

import java.time.LocalDate;

public interface WaveData {

    SurfConditions getLargestWaveSurfConditions(LocalDate date);
}
