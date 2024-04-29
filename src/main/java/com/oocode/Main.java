package com.oocode;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class Main {
  public static void main(String[] args) throws Exception {

    HttpClient httpClient = new BasicHttpClient();

    if (args.length == 2) {
      createPage(args[0], LocalDate.parse(args[1]), httpClient);
    } else {
      createPage("https://apps.des.qld.gov.au/data-sets/waves/wave-7dayopdata.csv", LocalDate.now(), httpClient);
    }
  }

  private static void createPage(String url, LocalDate today, HttpClient httpClient) throws Exception {
    String response = httpClient.readUrl(url);

    WaveInfo extractedWaveInfo = extractWaveInfo(response);

    writeHtml(extractedWaveInfo);
  }

  private static void writeHtml(WaveInfo waveInfo) throws IOException {
    try (FileWriter myWriter = new FileWriter("index.html")) {
      String maxWaveSizeFormatted = String.format(Locale.ENGLISH, "%.3f", waveInfo.maxWaveSize());
      String dateFormatted = waveInfo.date().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

      myWriter.write(
          String.format("<html><body>You should have been at %s on %s - it was gnarly - waves up to %sm!</body></html>",
              waveInfo.location(), dateFormatted, maxWaveSizeFormatted));
    }
  }

  private static WaveInfo extractWaveInfo(String response) throws IOException, CsvException {
    List<String[]> result;
    try (CSVReader reader = new CSVReader(new StringReader(response))) {
      result = reader.readAll().stream().skip(2).collect(toList());
    }
    String[] strings = result.stream().max(comparing(o -> Double.valueOf(o[7]))).orElse(null);

    LocalDateTime date = LocalDateTime.ofEpochSecond(parseInt(strings[2]), 0, ZoneOffset.ofHours(10));

    return new WaveInfo(strings[0], date, Double.parseDouble(strings[7]));
  }
}

