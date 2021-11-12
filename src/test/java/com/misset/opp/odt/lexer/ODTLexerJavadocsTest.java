package com.misset.opp.odt.lexer;

import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.misset.opp.testCase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

public class ODTLexerJavadocsTest extends ODTLexerTestCase {

    @Test
    void testJavaDoc() {
        final String content =
                "/**\n" +
                " * Some text \n" +
                " * @param $variable (string)\n" +
                " */\n" +
                "DEFINE QUERY query($variable) => 'Hello world';";
        noBadCharacter(content);
        hasElement(content, JavaDocElementType.DOC_COMMENT);
    }

}
