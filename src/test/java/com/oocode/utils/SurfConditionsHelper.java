package com.oocode.utils;

import com.oocode.Location;
import com.oocode.SurfConditions;

import java.time.LocalDateTime;

public class SurfConditionsHelper {

    public static SurfConditions createSurfConditions(String locationName, double lat, double lon, LocalDateTime dateTime, String waveSize) {
        // Helper to create instances of SurfConditions for testing, essentially a factory method
        Location location = new Location(locationName, lat, lon);
        return new SurfConditions(location, dateTime, waveSize);
    }
}
