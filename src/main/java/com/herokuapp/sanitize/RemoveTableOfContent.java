package com.herokuapp.sanitize;

import static com.herokuapp.sanitize.SanitizerUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoveTableOfContent implements Sanitizer {

    private final String REGEX = ".*\\.\\.\\.\\.\\..*";
    private final static String[] TABLE_OF_CONTENT = {"table des matieres", "table des matières", "table des matières", "table of content"};

    @Override
    public String[] sanitize(String[] lines) {
        return Optional.of(lines)
            .map(this::removeTableOfContentParagraphe)
            .map(this::removeLinesWithFiveDots)
            .orElse(lines);
    }

    private String[] removeTableOfContentParagraphe(String[] lines) {
        int beginTableIndex = indexOfFirst(lines, TABLE_OF_CONTENT);
        if(beginTableIndex == 0) {
            return lines;
        }
        int lastTableIndex = lastIndexOfTableOfContent(lines, beginTableIndex);
        log.info("table of content paragraphe found between index " + beginTableIndex + " and index " + lastTableIndex);
        return removeLinesInRange(lines, beginTableIndex, lastTableIndex);
    }

    private String[] removeLinesWithFiveDots(String[] lines) {
        int beginTableIndex = firstIndexOfFiveDots(lines);
        if(beginTableIndex == 0) {
            return lines;
        }
        int lastTableIndex = lastIndexOfFiveDots(lines);
        log.info("table of content with five dots found between index " + beginTableIndex + " and index " + lastTableIndex);
        return removeTableOfContentInRange(lines, beginTableIndex, lastTableIndex);
    }

    private int lastIndexOfTableOfContent(String[] lines, int beginTableIndex) {
        for(int i = beginTableIndex+1; i < lines.length; i++) {
            if(!Character.isDigit(lines[i].charAt(lines[i].length()-1))) { // first line that does not ends wit a number
                return i-1;
            }
        }
        return beginTableIndex;
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

    private int firstIndexOfFiveDots(String[] lines) {
        for(int i = 0; i < lines.length; i++) {
            if(lines[i].matches(REGEX)) {
                return i;
            }
        }
        return 0;
    }

    private int lastIndexOfFiveDots(String[] lines) {
        for(int i = lines.length-1; i > 0; i--) {
            if(lines[i].matches(REGEX)) {
                return i;
            }
        }
        return 0;
    }

}
