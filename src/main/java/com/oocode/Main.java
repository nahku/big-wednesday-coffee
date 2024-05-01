package com.oocode;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;

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
    String generatedHtml = HtmlGenerator.generateHtmlFromWaveInfo(extractedWaveInfo);
    writeHtml(generatedHtml);
  }

  private static void writeHtml(String htmlContent) throws IOException {
    try (FileWriter myWriter = new FileWriter("index.html")){
    myWriter.write(htmlContent);
    }
  }

  private static WaveInfo extractWaveInfo(String waveCsvData) throws IOException, CsvException {
    List<String[]> result;
    CSVReader reader = new CSVReader(new StringReader(waveCsvData));
    result = reader.readAll().stream().skip(2).toList();
    String[] strings = result.stream().max(comparing(o -> Double.valueOf(o[7]))).orElse(null);

    LocalDateTime date = LocalDateTime.ofEpochSecond(parseInt(strings[2]), 0, ZoneOffset.ofHours(10));

    return new WaveInfo(strings[0], date, strings[7]);
  }
}

