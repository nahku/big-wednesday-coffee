package com.oocode;

import com.opencsv.CSVReader;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;

public class QueenslandApiWaveData implements ApiWaveData {

    private List<String[]> waveDataRows;

    public QueenslandApiWaveData(String waveCsvData) {
        try (CSVReader reader = new CSVReader(new StringReader(waveCsvData))){
            List<String[]> dataRows = reader.readAll();

            if (dataRows.size() < 3) {
                throw new IllegalArgumentException("Invalid CSV data: Expected at least 3 rows in the input.");
            }

            waveDataRows = skipHeader(dataRows);

            if (waveDataRows.get(0).length < 8) {
                throw new IllegalArgumentException("Invalid CSV data: Expected at least 8 columns in the input.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract wave information from input.", e);
        }
    }

    public WaveInfo getMaxWaveInfo() {
        String[] maxWaveSizeRow = getRowWithMaxWaveSize();

        LocalDateTime date = LocalDateTime.ofEpochSecond(parseInt(maxWaveSizeRow[2]), 0, ZoneOffset.ofHours(10));

        return new WaveInfo(maxWaveSizeRow[0], date, maxWaveSizeRow[7]);
    }

    private static List<String[]> skipHeader(List<String[]> csvRows){
        return csvRows.stream().skip(2).toList();
    }

    private String[] getRowWithMaxWaveSize(){
        return waveDataRows.stream().max(comparing(o -> Double.valueOf(o[7]))).orElse(null);
    }
}
