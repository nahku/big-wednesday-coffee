package com.oocode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileWriterTest {

    @Test
    public void canWriteTextToFile() throws IOException {

        String fileName = "test.txt";
        String content = "Hello World!";

        FileHandlingHelper.deleteFileIfItExists(fileName);

        FileWriter.writeToFile(content, fileName);
        String output = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
        assertThat(output, equalTo(content));

        FileHandlingHelper.deleteFileIfItExists(fileName);
    }

    @Test
    public void canWriteEmptyFile() throws IOException {

        String fileName = "sample_filename.txt";
        String content = "";

        FileHandlingHelper.deleteFileIfItExists(fileName);

        FileWriter.writeToFile(content, fileName);
        String output = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
        assertThat(output, equalTo(content));

        FileHandlingHelper.deleteFileIfItExists(fileName);
    }

    // can write multiline
    @Test
    public void canWriteMultilineStringToFile() throws IOException {

        String fileName = "test.txt";
        String content = "Hello World!\nabcdefg\nline3";

        FileHandlingHelper.deleteFileIfItExists(fileName);

        FileWriter.writeToFile(content, fileName);
        String output = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
        assertThat(output, equalTo(content));

        FileHandlingHelper.deleteFileIfItExists(fileName);
    }

    @Test
    public void canWriteToHtmlFile() throws IOException {

        String fileName = "test.html";
        String content = "<html><body>You should have been at Location D on Sunday - it was gnarly - waves up to 1.200m!</body></html>";

        FileHandlingHelper.deleteFileIfItExists(fileName);

        FileWriter.writeToFile(content, fileName);
        String output = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
        assertThat(output, equalTo(content));

        FileHandlingHelper.deleteFileIfItExists(fileName);
    }

}
