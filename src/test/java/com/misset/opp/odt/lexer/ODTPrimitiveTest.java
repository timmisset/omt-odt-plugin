package com.misset.opp.odt.lexer;

import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.testcase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ODTPrimitiveTest extends ODTLexerTestCase {

    @Test
    void testPrimitiveString() {
        assertTrue(hasElement("$param (string)", ODTTypes.PRIMITIVE));
    }

    @Test
    void testPrimitiveNumber() {
        assertTrue(hasElement("$param (number)", ODTTypes.PRIMITIVE));
    }
}
