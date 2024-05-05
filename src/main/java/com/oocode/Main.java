package com.oocode;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

  private record WaveDaysInput(String url, LocalDate date) {}

  public static void main(String[] args) {

    HttpClient httpClient = new BasicHttpClient();

    WaveDaysInput waveDaysInput = parseInputArgs(args);

    createPage(waveDaysInput, httpClient);
  }

  private static void createPage(WaveDaysInput waveDaysInput, HttpClient httpClient) {

    String waveCsvData = httpClient.readUrl(waveDaysInput.url());
    WaveData waveData = new QueenslandApiWaveData(waveCsvData);
    SurfConditions extractedSurfConditions = BiggestWaveSurfConditionsExtractor.extractSurfConditions(waveData, waveDaysInput.date());
    String generatedHtml = SurfConditionsHtmlGenerator.generateHtmlFromSurfConditions(extractedSurfConditions);
    FileWriter.writeToFile(generatedHtml, "index.html");
  }

  private static WaveDaysInput parseInputArgs(String[] args) {
    LocalDate date;
    String url;
    if (args.length == 0) {
      url = "https://apps.des.qld.gov.au/data-sets/waves/wave-7dayopdata.csv";
      date = LocalDate.now();
    } else if (args.length == 2) {
      url = args[0];
      date = parseDate(args[1]);
    }
    else{
      throw new IllegalArgumentException("Either provide url and current date or no arguments.");
    }

    return new WaveDaysInput(url, date);
  }

  private static LocalDate parseDate(String date){
    try{
      return LocalDate.parse(date);
    }
    catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Provided date could not be parsed.", e);
    }
  }
}

