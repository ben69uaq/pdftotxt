package com.herokuapp.sanitize;

import java.util.Arrays;

public class RemoveTableOfContent implements Sanitizer {

    @Override
    public String[] sanitize(String[] lines) {
        return Arrays.stream(lines)
        .filter(line -> !line.matches(".*\\.\\.\\.\\..*"))
        .toArray(String[]::new);
    }
}
