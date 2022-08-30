package com.misset.opp.omt.util;

import com.intellij.openapi.util.TextRange;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

    private PatternUtil() {
        // empty constructor
    }

    public static Optional<TextRange> getTextRange(String text, Pattern pattern, int group) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find() && matcher.group(group) != null) {
            return Optional.of(new TextRange(matcher.start(group), matcher.end(group)));
        }
        return Optional.empty();
    }

    public static String getText(String text, Pattern pattern, int group) {
        return getTextRange(text, pattern, group)
                .orElse(TextRange.EMPTY_RANGE)
                .substring(text);
    }

}
