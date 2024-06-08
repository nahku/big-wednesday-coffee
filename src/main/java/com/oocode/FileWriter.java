package com.oocode;

import java.io.IOException;

public class FileWriter {

    public static void writeToFile(String content, String filename) {
        try (java.io.FileWriter myWriter = new java.io.FileWriter(filename)) {
            myWriter.write(content);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred reading file: " + filename, e);
        }
    }
}
