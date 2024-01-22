package com.ben69uaq.sanitize;

import java.util.function.UnaryOperator;

import org.apache.commons.lang3.StringUtils;

public interface Sanitizer extends UnaryOperator<String[]> {

    String[] apply(String[] lines);

    default String[] sanitize(String[] lines, String rules) {
        if(StringUtils.isNotBlank(rules) && rules.contains(this.getClass().getSimpleName())) {
            return apply(lines);
        }
        return lines;
    }
}
