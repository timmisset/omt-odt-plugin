package com.misset.opp.util;

import com.intellij.openapi.util.TextRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UriPatternUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "http://ontology/ClassA",
            "<http://ontology/ClassA>",
            "https://ontology/ClassA",
            "http://ontology#ClassA",
            "(<http://ontology#ClassA>)",
    })
    void testIsUri(String value) {
        assertTrue(UriPatternUtil.isUri(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "someText",
            ":schemalessCurie",
            "prefix:localname"
    })
    void testIsNotUri(String value) {
        assertFalse(UriPatternUtil.isUri(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ":schemalessCurie",
            "prefix:localname"
    })
    void testIsCurie(String value) {
        assertTrue(UriPatternUtil.isCurie(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://ontology/ClassA",
            "<http://ontology/ClassA>",
            "https://ontology/ClassA",
            "http://ontology#ClassA",
            "(<http://ontology#ClassA>)",
    })
    void testIsNotCurie(String value) {
        assertFalse(UriPatternUtil.isCurie(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://ontology/ClassA",
            "<http://ontology/ClassA>",
            "https://ontology/ClassA",
            "http://ontology#ClassA",
            "(<http://ontology#ClassA>)",
            "prefix:ClassA",
            ":ClassA",
            "(prefix:ClassA)"
    })
    void testGetLocalname(String value) {
        assertEquals("ClassA", UriPatternUtil.getLocalname(value));
    }

    @Test
    void testGetNamespace() {
        assertEquals("http://ontology/", UriPatternUtil.getNamespace("http://ontology/ClassA"));
        assertEquals("http://ontology/", UriPatternUtil.getNamespace("<http://ontology/ClassA>"));
        assertEquals("https://ontology/", UriPatternUtil.getNamespace("https://ontology/ClassA"));
        assertEquals("http://ontology#", UriPatternUtil.getNamespace("http://ontology#ClassA"));
        assertEquals("http://ontology#", UriPatternUtil.getNamespace("(<http://ontology#ClassA>)"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "prefix:ClassA",
            "(prefix:ClassA)"
    })
    void testGetPrefixRange(String value) {
        TextRange prefixRange = UriPatternUtil.getPrefixRange(value);
        assertEquals("prefix", prefixRange.substring(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://ontology/ClassA",
            "<http://ontology/ClassA>",
            "https://ontology/ClassA",
            "http://ontology#ClassA",
            "(<http://ontology#ClassA>)",
            "prefix:ClassA",
            ":ClassA",
            "(prefix:ClassA)"
    })
    void testGetLocalnameRange(String value) {
        TextRange prefixRange = UriPatternUtil.getLocalnameRange(value);
        assertEquals("ClassA", prefixRange.substring(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://ontology/ClassA",
            "<http://ontology/ClassA>",
            "(<http://ontology/ClassA>)"
    })
    void testGetNamespaceRange(String value) {
        TextRange prefixRange = UriPatternUtil.getNamespaceRange(value);
        assertEquals("http://ontology/", prefixRange.substring(value));
    }
}
