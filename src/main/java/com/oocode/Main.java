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

    String[] output = generateOutput(response);

    writeHtml(output);
  }

  private static void writeHtml(String[] strings) throws IOException {
    try (FileWriter myWriter = new FileWriter("index.html")) {
      LocalDateTime foo = LocalDateTime.ofEpochSecond(parseInt(strings[2]), 0, ZoneOffset.ofHours(10));
      myWriter.write(
          String.format("<html><body>You should have been at %s on %s - it was gnarly - waves up to %sm!</body></html>",
              strings[0], foo.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH), strings[7]));
    }
  }

  private static String [] generateOutput(String response) throws IOException, CsvException {
    List<String[]> result;
    try (CSVReader reader = new CSVReader(new StringReader(response))) {
      result = reader.readAll().stream().skip(2).collect(toList());
    }
    String[] strings = result.stream().max(comparing(o -> Double.valueOf(o[7]))).orElse(null);
    return strings;
  }
}

