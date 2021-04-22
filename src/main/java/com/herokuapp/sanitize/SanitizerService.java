package com.herokuapp.sanitize;

import java.util.Optional;

public class SanitizerService {

    private final static String LINE_SEPARATOR = "\r\n";

    private final Sanitizer removePartBeforeIntroduction;
    private final Sanitizer removePartAfterConclusion;
    private final Sanitizer removeTableOfContent;
    private final Sanitizer removeReferenceParagraphe;

    public SanitizerService() {
        removePartBeforeIntroduction = new RemovePartBeforeIntroduction();
        removePartAfterConclusion = new RemovePartAfterConclusion();
        removeTableOfContent = new RemoveTableOfContent();
        removeReferenceParagraphe = new RemoveReferenceParagraphe();
    }

    public String sanitize(final String input) {
        return Optional.of(input)
        .map(this::splitLines)
        .map(removeTableOfContent::sanitize)
        .map(removePartBeforeIntroduction::sanitize)
        .map(removePartAfterConclusion::sanitize)
        .map(removeReferenceParagraphe::sanitize)
        .map(this::joinLines)
        .orElseThrow(() -> new RuntimeException("Error while sanitizing document"));
    }

    private String[] splitLines(String input) {
        return input.split(LINE_SEPARATOR);
    }

    private String joinLines(String[] input) {
        return String.join(LINE_SEPARATOR, input);
    }


}