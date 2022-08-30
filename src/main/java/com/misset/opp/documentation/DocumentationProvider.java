package com.misset.opp.documentation;

import com.intellij.lang.documentation.DocumentationMarkup;

public class DocumentationProvider {

    private DocumentationProvider() {
        // empty constructor
    }

    /**
     * Creates a key/value row for the rendered documentation.
     */
    public static void addKeyValueSection(String key, String value, StringBuilder sb) {
        sb.append(DocumentationMarkup.SECTION_HEADER_START);
        sb.append(key);
        sb.append(DocumentationMarkup.SECTION_SEPARATOR);
        sb.append("<p>");
        sb.append(value);
        sb.append(DocumentationMarkup.SECTION_END);
    }

    public static String getKeyValueSection(String key, String value) {
        return DocumentationMarkup.SECTION_HEADER_START +
                key +
                DocumentationMarkup.SECTION_SEPARATOR +
                "<p>" +
                value +
                DocumentationMarkup.SECTION_END;
    }
}
