package com.herokuapp.sanitize;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoveTableOfContent implements Sanitizer {

    private final String REGEX = ".*\\.\\.\\.\\.\\..*";

    @Override
    public String[] sanitize(String[] lines) {
        int firstIndex = firstIndexOfTableOfContent(lines);
        if(firstIndex == 0) {
            log.info("no table of content found");
            return lines;
        }
        int lastIndex = lastIndexOfTableOfContent(lines);
        log.info("table of content found between index " + firstIndex + " and index " + lastIndex);
        return removeTableOfContentInRange(lines, firstIndex, lastIndex);
    }

    private String[] removeTableOfContentInRange(String[] lines, int firstIndex, int lastIndex) {
        List<String> output = new ArrayList<String>();
        for(int i = 0; i < lines.length; i++) {
            if(i < firstIndex || i > lastIndex) {
                output.add(lines[i]);
            }
            else if(!lines[i].matches(REGEX) && !lines[i-1].matches(REGEX) && !lines[i+1].matches(REGEX)) {
                output.add(lines[i]);
            }
        }
        return output.stream().toArray(String[]::new);
    }

    private int firstIndexOfTableOfContent(String[] lines) {
        for(int i = 0; i < lines.length; i++) {
            if(lines[i].matches(REGEX)) {
                return i;
            }
        }
        return 0;
    }

    private int lastIndexOfTableOfContent(String[] lines) {
        for(int i = lines.length-1; i > 0; i--) {
            if(lines[i].matches(REGEX)) {
                return i;
            }
        }
        return 0;
    }

}
