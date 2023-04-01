package com.misset.opp.documentation;

import com.intellij.lang.documentation.DocumentationMarkup;

public class DocumentationProvider {

    private DocumentationProvider() {
        // empty constructor
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
