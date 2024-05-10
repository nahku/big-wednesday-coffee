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
        String threeDecimalsFloatRegex = "?\\d+\\.\\d{3}"; // Matches a non-negative floating point number with 3 decimal places
        String fiveDecimalsFloatRegex = "-?\\d+\\.\\d{5}"; // Matches a floating point number with 3 decimal places
        String latLonRegex = fiveDecimalsFloatRegex + "," + fiveDecimalsFloatRegex; // Matches latitude and longitude separated by a comma

        String googleMapsLinkRegex =  "<a target=\"_blank\" href=\"http://www.google.com/maps/place/" + latLonRegex + "/@" + latLonRegex + ",12z\">.*</a>";

        String waveSizeTextRegex = "You should have been at " + googleMapsLinkRegex + " on .* - it was gnarly - waves up to " + threeDecimalsFloatRegex + "m!";

        Pattern responseRegex = Pattern.compile(
                "<html><body>" +
                waveSizeTextRegex +
                "</body></html>"
        );

        Main.main(new String[]{});
        String output = Files.readString(Paths.get("index.html"), StandardCharsets.UTF_8);

        Matcher matcher = responseRegex.matcher(output);
        assertTrue("Output does not match expected format.", matcher.matches());

    }

    @BeforeEach
    @AfterEach
    public void deleteOutputIfItExists() {
        FileHandlingHelper.deleteFileIfItExists("index.html");
    }
}
