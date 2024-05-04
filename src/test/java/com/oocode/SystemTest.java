package com.oocode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class SystemTest {

    @Test
    public void canProduceOutputBasedOnQueenslandApi() throws IOException {

        Pattern regex = Pattern.compile("<html><body>You should have been at .* on .* - it was gnarly - waves up to [0-9]+\\.[0-9]+m!</body></html>");

        Main.main(new String[]{});
        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        Matcher matcher = regex.matcher(output);
        assertTrue("Output does not match expected format.", matcher.matches());

    }

    @BeforeEach
    @AfterEach
    public void deleteOutputIfItExists() {
        FileHandlingHelper.deleteFileIfItExists("index.html");
    }
}
