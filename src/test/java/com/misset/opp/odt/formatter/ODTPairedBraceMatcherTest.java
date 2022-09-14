package com.misset.opp.odt.formatter;

import com.misset.opp.odt.testcase.ODTFormattingTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTPairedBraceMatcherTest extends ODTFormattingTestCase {

    @Test
    void testInsertsCurlyBracketPair() {
        configureByText("IF true {<caret>");
        enter();
        Assertions.assertEquals("IF true {\n" +
                getIndent() + "\n" +
                "}", getDocumentText());
    }
}
