package com.ben69uaq.sanitize;

import java.util.Optional;

public class SanitizerService {

    private final String separator = System.getProperty("line.separator");

    private final Sanitizer removePartBeforeIntroduction;
    private final Sanitizer removePartAfterConclusion;
    private final Sanitizer removeTableOfContent;
    private final Sanitizer removeReferenceParagraphe;
    private final Sanitizer removeLinesWithoutWord;

    public SanitizerService() {
        removePartBeforeIntroduction = new RemovePartBeforeIntroduction();
        removePartAfterConclusion = new RemovePartAfterConclusion();
        removeTableOfContent = new RemoveTableOfContent();
        removeReferenceParagraphe = new RemoveReferenceParagraphe();
        removeLinesWithoutWord = new RemoveLinesWithoutWord();
    }

    public String sanitize(final String input, final String rules) {
        return Optional.of(input)
        .map(this::splitLines)
        .map(lines -> removeLinesWithoutWord.sanitize(lines, rules))
        .map(lines -> removeTableOfContent.sanitize(lines, rules))
        .map(lines -> removePartBeforeIntroduction.sanitize(lines, rules))
        .map(lines -> removePartAfterConclusion.sanitize(lines, rules))
        .map(lines -> removeReferenceParagraphe.sanitize(lines, rules))
        .map(this::joinLines)
        .orElseThrow(() -> new RuntimeException("Error while sanitizing document"));
    }

    private String[] splitLines(String input) {
        return input.split(separator);
    }

    private String joinLines(String[] input) {
        return String.join(separator, input);
    }

}