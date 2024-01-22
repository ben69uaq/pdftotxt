package com.ben69uaq.sanitize.helper;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionHelper {

    public static boolean startsOrEndsWithOneOf(String line, String[] keywords) {
        return Arrays.stream(keywords).anyMatch(keyword -> startsOrEndsWith(line, keyword));
    }

    public static boolean startsOrEndsWith(String line, String keyword) {
        String cleanLine = line.toLowerCase().trim();
        if(cleanLine.equals(keyword) || cleanLine.startsWith(keyword) || cleanLine.endsWith(keyword)) {
            log.info("[startsOrBeginWith] <" + keyword + "> found in <" + line + ">");
            return true;
        }
        return false;
    }

    public static boolean isTitle(String line) {
        if(line.length() < 40 && line.trim().matches(".*[0-9A-zÀ-ú]$")) {
            log.info("[isTitle] title <" + line + "> found");
            return true;
        }
        return false;
    }

    public static boolean notEndsWithNumber(String line) {
        if(!Character.isDigit(line.charAt(line.length() - 1))) {
            log.info("[notEndsWithNumber] no number found at the end of <" + line + ">");
            return true;
        }
        return false;
    }
}
