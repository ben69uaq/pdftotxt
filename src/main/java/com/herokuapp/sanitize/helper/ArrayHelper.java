package com.herokuapp.sanitize.helper;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArrayHelper {

    public static String[] keepLineInRange(String[] lines, int from, int to) {
        log.info("[keepLineInRange] from " + from + " to " + to);
        return Arrays.copyOfRange(lines, from, to);
    }

    public static String[] removeLinesInRange(String[] lines, int from, int to) {
        String[] firstPart = Arrays.copyOfRange(lines, 0, from);
        String[] secondPart = Arrays.copyOfRange(lines, to + 1, lines.length);
        log.info("[removeLinesInRange] from " + from + " to " + to);
        return ArrayUtils.addAll(firstPart, secondPart);
    }
}
