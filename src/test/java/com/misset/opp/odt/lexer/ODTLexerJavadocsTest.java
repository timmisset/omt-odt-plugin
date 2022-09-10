package com.misset.opp.odt.lexer;

import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.misset.opp.odt.testcase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

class ODTLexerJavadocsTest extends ODTLexerTestCase {

    @Test
    void testJavaDoc() {
        final String content =
                "/**\n" +
                        " * Some text \n" +
                        " * @param $variable (string)\n" +
                        " */\n" +
                        "DEFINE QUERY query($variable) => 'Hello world';";
        assertFalse(hasBadCharacter(content));
        assertTrue(hasElement(content, JavaDocElementType.DOC_COMMENT));
    }

}
