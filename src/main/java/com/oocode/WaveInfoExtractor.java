package com.oocode;

import com.opencsv.CSVReader;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;

public class WaveInfoExtractor {

    public static WaveInfo extractWaveInfo(String waveCsvData) {
        try (CSVReader reader = new CSVReader(new StringReader(waveCsvData))){
            List<String[]> result = reader.readAll();

            if (result.size() < 3) {
                throw new IllegalArgumentException("Invalid CSV data: Expected at least 3 rows in the input.");
            }

            List<String[]> filteredResult = result.stream().skip(2).toList();

            if (filteredResult.get(0).length < 8) {
                throw new IllegalArgumentException("Invalid CSV data: Expected at least 8 columns in the input.");
            }

            String[] strings = filteredResult.stream().max(comparing(o -> Double.valueOf(o[7]))).orElse(null);
            LocalDateTime date = LocalDateTime.ofEpochSecond(parseInt(strings[2]), 0, ZoneOffset.ofHours(10));

            return new WaveInfo(strings[0], date, strings[7]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract wave information from input.", e);
        }
    }
}
