package com.oocode;

import com.opencsv.CSVReader;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.lang.Integer.parseInt;

public class QueenslandApiWaveData implements WaveData {

    private final List<SurfConditions> surfConditions;

    public QueenslandApiWaveData(String waveCsvData) {
        try (CSVReader reader = new CSVReader(new StringReader(waveCsvData))){
            List<String[]> dataRows = reader.readAll();

            if (dataRows.size() < 3) {
                throw new IllegalArgumentException("Invalid CSV data: Expected at least 3 rows in the input.");
            }

            List<String[]> waveDataRows = skipHeader(dataRows);

            if (waveDataRows.getFirst().length < 8) {
                throw new IllegalArgumentException("Invalid CSV data: Expected at least 8 columns in the input.");
            }

            surfConditions = waveDataRows.stream().map(row -> createSurfConditionsFromDataRow(row)).toList();

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract wave information from input.", e);
        }
    }

    @Override
    public List<SurfConditions> getSurfConditions(LocalDate fromDate, LocalDate toDate) {
        return surfConditions.stream().filter(conditions -> isBetween(fromDate, toDate, conditions.getDate())).toList();
    }

    private static boolean isBetween(LocalDate startDate, LocalDate endDate, LocalDate dateToVerify) {
        return !dateToVerify.isBefore(startDate) && !dateToVerify.isAfter(endDate);
    }

    private static List<String[]> skipHeader(List<String[]> csvRows){
        return csvRows.stream().skip(2).toList();
    }

    private static SurfConditions createSurfConditionsFromDataRow(String[] row){
        Location location = createLocationFromDataRow(row);
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(parseInt(row[2]), 0, ZoneOffset.ofHours(10));
        String waveSize = row[7];
        return new SurfConditions(location, dateTime, waveSize);
    }

    private static Location createLocationFromDataRow(String[] row){
        return new Location(row[0], Double.parseDouble(row[4]), Double.parseDouble(row[5]));
    }

}
