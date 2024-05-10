package com.oocode;

import com.oocode.utils.FileHandlingHelper;
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
        Pattern outputPattern = buildOutputPattern();

        Main.main(new String[]{});
        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        Matcher matcher = outputPattern.matcher(output);
        assertTrue("Output does not match expected format.", matcher.matches());

    }

    private static Pattern buildOutputPattern() {
        String threeDecimalsFloatRegex = "?\\d+\\.\\d{3}"; // Matches a non-negative floating point number with 3 decimal places
        String fiveDecimalsFloatRegex = "-?\\d+\\.\\d{5}"; // Matches a floating point number with 3 decimal places
        String latLonRegex = fiveDecimalsFloatRegex + "," + fiveDecimalsFloatRegex; // Matches latitude and longitude separated by a comma

        String googleMapsLinkRegex = "<a target=\"_blank\" href=\"http://www.google.com/maps/place/" + latLonRegex + "/@" + latLonRegex + ",12z\">.*</a>";

        String fullOutput = "<html><body>You should have been at " + googleMapsLinkRegex + " on .* - it was gnarly - waves up to " + threeDecimalsFloatRegex + "m!</body></html>";

        return Pattern.compile(fullOutput);
    }

    @BeforeEach
    @AfterEach
    public void deleteOutputIfItExists() {
        FileHandlingHelper.deleteFileIfItExists("index.html");
    }
}
