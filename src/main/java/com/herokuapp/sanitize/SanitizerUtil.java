package com.herokuapp.sanitize;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class SanitizerUtil {
    
    static int indexOfFirst(String[] lines, String[] keywords) {
        for(int i=0; i<lines.length; i++) {
            if(startsOrBeginWithOneOf(lines[i].toLowerCase().trim(), keywords)) {
                log.info("[indexOfFirst] first keyword found at index " + i);
                return i;
            }
        }
        return 0;
    }

    static int indexOfLast(String[] lines, String[] keywords) {
        for(int i=lines.length-1; i>0; i--) {
            if(startsOrBeginWithOneOf(lines[i].toLowerCase().trim(), keywords)) {
                log.info("[indexOfLast] last keyword found at index " + i);
                return i;
            }
        }
        return lines.length;
    }

    static boolean startsOrBeginWithOneOf(String line, String[] keywords) {
        for(String keyword:keywords) {
            if(line.startsWith(keyword) || line.endsWith(keyword)) {
                log.info("[startsOrBeginWithOneOf] <" + keyword + "> found in <" + line + ">");
                return true;
            }
        }
        return false;
    }

    static int indexOfNextTitle(String[] lines, int startIndex) {
        for(int i=startIndex; i<lines.length; i++) {
            if(lines[i].length() < 30 && lines[i].trim().matches(".*[0-9A-zÀ-ú]$")) {
                log.info("[indexOfNextTitle] next title <" + lines[i] + "> found with index " + i);
                return i;
            }
        }
        log.info("[indexOfNextTitle] no next title found, returning end index " + lines.length);
        return lines.length;
    }
}
