package com.oocode.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandlingHelper {

    public static void deleteFileIfItExists(String fileName) {
        // Deletes the (potential) previous output to have a clean test environment
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not delete " + fileName, e);
            }
        }
    }
}
