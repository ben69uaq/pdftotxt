package com.herokuapp.sanitize;

import java.util.Arrays;

public class RemoveLinesWithoutWord implements Sanitizer {

    private final String REGEX1 = ".*[A-zÀ-ú]{4}.*"; // at least 4 char

    @Override
    public String[] sanitize(String[] lines) {
        return Arrays.stream(lines)
        .filter(this::hasWord)
        .toArray(String[]::new);
    }

    private boolean hasWord(String line) {
        if(line.matches(REGEX1) || endsWithPunctuation(line)) {
            return true;
        }
        return false;
    }

    private boolean endsWithPunctuation(String line) {
        return line.endsWith(".")
            || line.endsWith(",")
            || line.endsWith(":")
            || line.endsWith("?")
            || line.endsWith("!")
            || line.endsWith(";");
    }
}
