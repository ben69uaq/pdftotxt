package com.herokuapp.sanitize;

import static com.herokuapp.sanitize.SanitizerUtil.*;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    
    private String[] removeLinesInRange(String[] lines, int referenceIndex, int nextTitleIndex) {
        List<String> output = new ArrayList<String>();
        for(int i=0; i<lines.length; i++) {
            if(!(i >= referenceIndex && i < nextTitleIndex)) {
                output.add(lines[i]);
            }
        }
        log.info("line from index " + referenceIndex + " to index " + nextTitleIndex + " removed");
        return output.stream().toArray(String[]::new);
    }
}
