package com.herokuapp.sanitize;

import static com.herokuapp.sanitize.helper.IndexHelper.*;
import static com.herokuapp.sanitize.helper.ArrayHelper.*;

public class RemoveReferenceParagraphe implements Sanitizer {

    private final static String[] KEYWORD = { "références", "references","bibliographie", "bibliography", "références bibliographiques" };

    @Override
    public String[] apply(String[] lines) {
        return removeReferenceParagraphe(lines);
    }

    public String[] removeReferenceParagraphe(String[] lines) {
        int referenceIndex = indexOfFirstKeyword(lines, KEYWORD);
        if (referenceIndex == -1) {
            return lines;
        }
        int nextTitleIndex = indexOfNextTitle(lines, referenceIndex + 3);
        if (nextTitleIndex == -1) {
            return lines;
        }
        String[] outputLines = removeLinesInRange(lines, referenceIndex, nextTitleIndex - 1);
        return removeReferenceParagraphe(outputLines);
    }
}
