package com.herokuapp.sanitize;

import static com.herokuapp.sanitize.SanitizerUtil.*;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemovePartBeforeIntroduction implements Sanitizer {

    private final static String[] INTRODUCTION = {"introduction"};
    private final static String[] ABSTRACT = {"abstract"};
    private final static String[] RESUME = {"résumé", "resume"};

    @Override
    public String[] sanitize(String[] lines) {
        int beginIndex = indexOfFirst(lines, INTRODUCTION);
        if(beginIndex == 0) {
            beginIndex = indexOfEndOfAbstract(lines);
        }
        log.info("line from begining to " + beginIndex + " removed");
        return Arrays.copyOfRange(lines, beginIndex, lines.length);
    }

    private int indexOfEndOfAbstract(String[] lines) {
        int abstractIndex = indexOfFirst(lines, ABSTRACT);
        if(abstractIndex != 0) {
            return indexOfNextTitle(lines ,abstractIndex);
        }
        int resumeIndex = indexOfFirst(lines, RESUME);
        if(resumeIndex != 0) {
            return indexOfNextTitle(lines ,resumeIndex);
        }
        return 0;
    }
    
}
