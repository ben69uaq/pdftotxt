package com.herokuapp;

import java.util.Arrays;

import org.apache.logging.log4j.util.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentSanitizer {

    private final static String LINE_SEPARATOR = "\r\n";
    static String sanitize(final String input) {
        String[] output = splitLines(input);
        output = removePartBeforeResumeOrIntroduction(output);
        output = removePartAfterConclusion(output);
        return joinLines(output);
    }

    private static String[] splitLines(String input) {
        return input.split(LINE_SEPARATOR);
    }

    private static String joinLines(String[] input) {
        return String.join(LINE_SEPARATOR, input);
    }

    static String removeTableMatiere(String line) {
        return line.matches(".*\\.\\.\\.\\..*") ? Strings.EMPTY : line;
    }

    static String[] removePartBeforeResumeOrIntroduction(String[] lines) {
        String[] keywords = {"resume", "résumé", "introduction"};
        int beginIndex = indexOfFirst(lines, keywords);
        return Arrays.copyOfRange(lines, beginIndex, lines.length);
    }

    static String[] removePartAfterConclusion(String[] lines) {
        String[] keywords = {"conclusion"};
        int conclusionIndex = indexOfLast(lines, keywords);
        int nextTitleIndex = indexOfNextTitle(lines, conclusionIndex + 1);
        return Arrays.copyOfRange(lines, 0, nextTitleIndex);
    }

    static int indexOfFirst(String[] lines, String[] keywords) {
        for(int i=0; i<lines.length; i++) {
            if(startsOrBeginWithOneOf(lines[i].toLowerCase(), keywords)) {
                return i;
            }
        }
        return 0;
    }

    static int indexOfLast(String[] lines, String[] keywords) {
        for(int i=lines.length-1; i>0; i--) {
            if(startsOrBeginWithOneOf(lines[i].toLowerCase(), keywords)) {
                return i;
            }
        }
        return lines.length;
    }

    static boolean startsOrBeginWithOneOf(String line, String[] keywords) {
        for(String keyword:keywords) {
            if(line.startsWith(keyword) || line.endsWith(keyword)) {
                log.info("<" + keyword + "> found in <" + line + ">");
                return true;
            }
        }
        return false;
    }

    static int indexOfNextTitle(String[] lines, int startIndex) {
        for(int i=startIndex; i<lines.length; i++) {
            if(lines[i].length() < 30 && lines[i].matches(".*[0-9A-zÀ-ú]$")) {
                log.info("<" + lines[i] + "> with index " + lines[i].length());
                return i;
            }
        }
        return lines.length;
    }
}