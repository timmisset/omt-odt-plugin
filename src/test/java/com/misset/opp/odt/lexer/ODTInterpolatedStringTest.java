package com.misset.opp.odt.lexer;

import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.testCase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

public class ODTInterpolatedStringTest extends ODTLexerTestCase {

    @Test
    void testInterpolatedString() {
        final String content =
                "`test ${ call }`";
        noBadCharacter(content);
        hasElement(content, ODTTypes.SYMBOL, ODTTypes.INTERPOLATED_STRING_START, ODTTypes.INTERPOLATED_STRING_END, ODTTypes.INTERPOLATION_START, ODTTypes.INTERPOLATION_END);
    }

}
