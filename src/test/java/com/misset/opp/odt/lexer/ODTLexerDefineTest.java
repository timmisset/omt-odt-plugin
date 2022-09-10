package com.misset.opp.odt.lexer;

import com.misset.opp.odt.testcase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ODTLexerDefineTest extends ODTLexerTestCase {

    @Test
    void testDefineQuery() {
        assertFalse(hasBadCharacter("DEFINE QUERY query => 'hello world';"));
    }

    @Test
    void testDefineQueryWithParameters() {
        assertFalse(hasBadCharacter("DEFINE QUERY query($paramA, $paramB) => 'hello world';"));
    }

    @Test
    void testDefineQueryWithWronglyNamedParameters() {
        assertTrue(hasBadCharacter("DEFINE QUERY query(paramA, paramB) => 'hello world';"));
    }

    @Test
    void testDefineCommand() {
        assertFalse(hasBadCharacter("DEFINE COMMAND command => { RETURN 'hello world'; }"));
    }

    @Test
    void testDefinePrefix() {
        assertFalse(hasBadCharacter("PREFIX abc: <http://abc.com>"));
        assertFalse(hasBadCharacter("PREFIX : <http://abc.com>"));
    }
}
