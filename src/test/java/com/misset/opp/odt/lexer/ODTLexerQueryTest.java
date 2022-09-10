package com.misset.opp.odt.lexer;

import com.misset.opp.odt.testcase.ODTLexerTestCase;
import org.junit.jupiter.api.Test;

import static com.misset.opp.odt.psi.ODTTypes.SCHEMALESS_IRI;

public class ODTLexerQueryTest extends ODTLexerTestCase {

    @Test
    void testQueryWithSchemalessIri() {
        final String content = "DEFINE QUERY query => <something> / prefix:localName;";
        noBadCharacter(content);
        hasElement(content, SCHEMALESS_IRI);
    }

}
