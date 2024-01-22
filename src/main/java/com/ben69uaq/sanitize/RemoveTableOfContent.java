package com.ben69uaq.sanitize;

import static com.ben69uaq.sanitize.helper.ArrayHelper.*;
import static com.ben69uaq.sanitize.helper.IndexHelper.*;

import java.util.Optional;

import com.ben69uaq.sanitize.helper.ConditionHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoveTableOfContent implements Sanitizer {

    private final String REGEX = ".*\\.\\.\\.\\.\\..*";
    private final String REGEX2 = ".*…….*";
    private final static String[] TABLE_OF_CONTENT = {"table des matieres", "table des matières", "table des matières", "table of content", "sommaire"};

    @Override
    public String[] apply(String[] lines) {
        return Optional.of(lines)
            .map(this::removeBasedOnPageNumber)
            .map(this::removeBasedOnDots)
            .orElse(lines);
    }

    private String[] removeBasedOnPageNumber(String[] lines) {
        int from = indexOfFirstKeyword(lines, TABLE_OF_CONTENT);
        if(from == -1) {
            return lines;
        }
        int to = indexOfNextMatching(lines, from + 3, ConditionHelper::isTitle);
        if(to == -1) {
            return lines;
        }
        log.info("table of content paragraphe removed from " + from + " to " + (to - 1));
        String[] outputLines = removeLinesInRange(lines, from, to - 1);
        return removeBasedOnPageNumber(outputLines);
    }

    private String[] removeBasedOnDots(String[] lines) {
        int from = indexOfNextMatching(lines, 0, line -> line.matches(REGEX) || line.matches(REGEX2));
        if(from == -1) {
            return lines;
        }
        int to = indexOfPreviousMatching(lines, lines.length - 1, line -> line.matches(REGEX) || line.matches(REGEX2));
        if(to == -1) {
            return lines;
        }
        log.info("table of content with five dots removed from " + from + " to " + to);
        return removeLinesInRange(lines, from, to);
    }

}
