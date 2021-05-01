package com.herokuapp.sanitize.helper;

import static com.herokuapp.sanitize.helper.ConditionHelper.startsOrEndsWithOneOf;

import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexHelper {
    
    public static int indexOfFirstKeyword(String[] lines, String[] keywords) {
        return indexOfNextMatching(lines, 0, line -> startsOrEndsWithOneOf(line, keywords));
    }

    public static int indexOfLastKeyword(String[] lines, String[] keywords) {
        return indexOfPreviousMatching(lines, lines.length-1, line -> startsOrEndsWithOneOf(line, keywords));
    }

    public static int indexOfNextTitle(String[] lines, int from) {
        return indexOfNextMatching(lines, from, ConditionHelper::isTitle);
    }

    public static int indexOfNextMatching(String[] lines, int from, Predicate<String> condition) {
        for(int i = from; i < lines.length; i++) {
            if(condition.test(lines[i])) {
                log.info("[indexOfNextMatching] matching at index " + i);
                return i;
            }
        }
        return -1;
    }

    public static int indexOfPreviousMatching(String[] lines, int from, Predicate<String> condition) {
        for(int i = from; i > 0; i--) {
            if(condition.test(lines[i])) {
                log.info("[indexOfPreviousMatching] matching at index " + i);
                return i;
            }
        }
        return -1;
    }
}
