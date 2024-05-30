package com.oocode;

import java.time.LocalDate;
import java.util.List;

public interface WaveData {

    List<SurfConditions> getSurfConditions(LocalDate fromDate, LocalDate toDate);
}
