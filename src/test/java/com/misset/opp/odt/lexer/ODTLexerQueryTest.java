package com.misset.opp.odt.lexer;

import com.misset.opp.odt.testcase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

import static com.misset.opp.odt.psi.ODTTypes.SCHEMALESS_IRI;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ODTLexerQueryTest extends ODTLexerTestCase {

    @Test
    void testQueryWithSchemalessIri() {
        final String content = "DEFINE QUERY query => <something> / prefix:localName;";
        assertFalse(hasBadCharacter(content));
        assertTrue(hasElement(content, SCHEMALESS_IRI));
    }

}
