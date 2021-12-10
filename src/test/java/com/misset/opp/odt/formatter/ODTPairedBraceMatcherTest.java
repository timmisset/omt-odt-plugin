package com.misset.opp.odt.formatter;

import com.misset.opp.testCase.ODTFormattingTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTPairedBraceMatcherTest extends ODTFormattingTestCase {

    @Test
    void testInsertsBracePair() {
        configureByText("IF true {<caret>");
        enter();
        Assertions.assertEquals("IF true {\n" +
                getIndent() + "\n" +
                "}", getDocumentText());
    }

}