package com.ben69uaq.sanitize;

import static com.ben69uaq.sanitize.helper.ArrayHelper.*;
import static com.ben69uaq.sanitize.helper.IndexHelper.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemovePartAfterConclusion implements Sanitizer {

    private final static String[] KEYWORD_ZERO = {"conclusion générale"};
    private final static String[] KEYWORD_FIRST = {"conclusions et perspectives"};
    private final static String[] KEYWORD_SECOND = {"conclusion", "conclusions"};

    @Override
    public String[] apply(String[] lines) {
        int from = indexOfEndOfConclusion(lines);
        if(from == -1) {
            return lines;
        }
        log.info("Conclusion found, part after index " + from + " removed");
        return keepLineInRange(lines, 0, from);
    }
    
    private int indexOfEndOfConclusion(String[] lines) {
        int indexZero = indexOfLastKeyword(lines, KEYWORD_ZERO);
        if(indexZero != -1) {
            return indexOfNextTitle(lines, indexZero + 3); // search title three line after conclusion keyword
        }
        int indexFirst = indexOfLastKeyword(lines, KEYWORD_FIRST);
        if(indexFirst != -1) {
            return indexOfNextTitle(lines, indexFirst + 3);
        }
        int indexSecond = indexOfLastKeyword(lines, KEYWORD_SECOND);
        if(indexSecond != -1) {
            return indexOfNextTitle(lines, indexSecond + 3);
        }
        return -1;
    }
    
}
