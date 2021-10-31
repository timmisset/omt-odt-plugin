package com.misset.opp.odt.lexer;

import com.misset.opp.testCase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

class ODTLexerDefineTest extends ODTLexerTestCase {

    @Test
    void testDefineQuery() {
        noBadCharacter("DEFINE QUERY query => 'hello world';");
    }

    @Test
    void testDefineQueryWithParameters() {
        noBadCharacter("DEFINE QUERY query($paramA, $paramB) => 'hello world';");
    }

    @Test
    void testDefineQueryWithWronglyNamedParameters() {
        hasBadCharacter("DEFINE QUERY query(paramA, paramB) => 'hello world';");
    }

    @Test
    void testDefineCommand() {
        noBadCharacter("DEFINE COMMAND command => { RETURN 'hello world'; }");
    }

    @Test
    void testDefinePrefix() {
        noBadCharacter("PREFIX abc: <http://abc.com>");
        noBadCharacter("PREFIX : <http://abc.com>");
    }
}
