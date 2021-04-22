package com.herokuapp.sanitize;

import static com.herokuapp.sanitize.SanitizerUtil.indexOfLast;
import static com.herokuapp.sanitize.SanitizerUtil.indexOfNextTitle;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemovePartAfterConclusion implements Sanitizer {

    private final static String[] KEYWORD = {"conclusion"};

    @Override
    public String[] sanitize(String[] lines) {
        int conclusionIndex = indexOfLast(lines, KEYWORD);
        int nextTitleIndex = indexOfNextTitle(lines, conclusionIndex + 1);
        log.info("line from " + nextTitleIndex + " to the end removed");
        return Arrays.copyOfRange(lines, 0, nextTitleIndex);
    }
    
}
