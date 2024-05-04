package com.oocode;

import java.time.LocalDate;

public class Main {
  public static void main(String[] args) {

    HttpClient httpClient = new BasicHttpClient();

    if (args.length == 2) {
      createPage(args[0], LocalDate.parse(args[1]), httpClient);
    } else {
      createPage("https://apps.des.qld.gov.au/data-sets/waves/wave-7dayopdata.csv", LocalDate.now(), httpClient);
    }
  }

  private static void createPage(String url, LocalDate today, HttpClient httpClient) {

    String waveCsvData = httpClient.readUrl(url);
    ApiWaveData waveData = new QueenslandApiWaveData(waveCsvData);
    SurfConditions extractedSurfConditions = SurfConditionsExtractor.extractWaveInfo(waveData);
    String generatedHtml = SurfConditionsHtmlGenerator.generateHtmlFromWaveInfo(extractedSurfConditions);
    FileWriter.writeToFile(generatedHtml, "index.html");
  }

}

