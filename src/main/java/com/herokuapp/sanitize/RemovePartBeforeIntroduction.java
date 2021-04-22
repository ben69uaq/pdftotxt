package com.herokuapp.sanitize;

import static com.herokuapp.sanitize.SanitizerUtil.indexOfFirst;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemovePartBeforeIntroduction implements Sanitizer {

    private final static String[] KEYWORD = {"introduction"};

    @Override
    public String[] sanitize(String[] lines) {
        int beginIndex = indexOfFirst(lines, KEYWORD);
        log.info("line from begining to " + beginIndex + " removed");
        return Arrays.copyOfRange(lines, beginIndex, lines.length);
    }
    
}
