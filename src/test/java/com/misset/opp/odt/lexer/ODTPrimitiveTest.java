package com.misset.opp.odt.lexer;

import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.testcase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

class ODTPrimitiveTest extends ODTLexerTestCase {

    @Test
    void testPrimitiveString() {
        hasElement("$param (string)", ODTTypes.PRIMITIVE);
    }

    @Test
    void testPrimitiveNumber() {
        hasElement("$param (number)", ODTTypes.PRIMITIVE);
    }
}
