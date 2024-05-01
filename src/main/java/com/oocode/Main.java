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

    String waveCsvData = httpClient.readUrl(url);
    WaveInfo extractedWaveInfo = WaveInfoExtractor.extractWaveInfo(waveCsvData);
    String generatedHtml = WaveInfoHtmlGenerator.generateHtmlFromWaveInfo(extractedWaveInfo);
    writeHtmlToFile(generatedHtml);
  }

  private static void writeHtmlToFile(String htmlContent) throws IOException {
    FileWriter myWriter = new FileWriter("index.html");
    myWriter.write(htmlContent);
    myWriter.close();
  }


}

