package com.herokuapp.sanitize;

import static com.herokuapp.sanitize.helper.IndexHelper.*;
import static com.herokuapp.sanitize.helper.ArrayHelper.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemovePartBeforeIntroduction implements Sanitizer {

    private final static String[] INTRODUCTION = {"introduction", "introduction générale"};
    private final static String[] ABSTRACT = {"abstract"};
    private final static String[] RESUME = {"résumé", "resume"};

    @Override
    public String[] apply(String[] lines) {
        int from = indexOfBeginingOfIntroduction(lines);
        if(from == -1) {
            return lines;
        }
        log.info("Introduction found, part before index " + from + " removed");
        return keepLineInRange(lines, from, lines.length);
    }

    private int indexOfBeginingOfIntroduction(String[] lines) {
        int introductionIndex = indexOfFirstKeyword(lines, INTRODUCTION);
        if(introductionIndex != -1) {
            return introductionIndex;
        }
        int abstractIndex = indexOfFirstKeyword(lines, ABSTRACT);
        if(abstractIndex != -1) {
            return indexOfNextTitle(lines ,abstractIndex + 3);
        }
        int resumeIndex = indexOfFirstKeyword(lines, RESUME);
        if(resumeIndex != -1) {
            return indexOfNextTitle(lines ,resumeIndex + 3);
        }
        return -1;
    }

}
