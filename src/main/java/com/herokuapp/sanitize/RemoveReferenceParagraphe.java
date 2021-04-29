package com.herokuapp.sanitize;

import static com.herokuapp.sanitize.SanitizerUtil.indexOfFirst;
import static com.herokuapp.sanitize.SanitizerUtil.indexOfNextTitle;
import static com.herokuapp.sanitize.SanitizerUtil.removeLinesInRange;

public class RemoveReferenceParagraphe implements Sanitizer {

    private final static String[] KEYWORD = {"références" , "references"};

    @Override
    public String[] sanitize(String[] lines) {
        String[] temp = lines;
        while(indexOfFirst(temp, KEYWORD) != 0) {
            int referenceIndex = indexOfFirst(temp, KEYWORD);
            int nextTitleIndex = indexOfNextTitle(temp, referenceIndex + 1);
            temp = removeLinesInRange(temp, referenceIndex, nextTitleIndex);
        }
        return temp;
    }
}
