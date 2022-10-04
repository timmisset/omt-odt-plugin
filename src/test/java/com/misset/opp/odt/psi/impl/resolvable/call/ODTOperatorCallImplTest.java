package com.misset.opp.odt.psi.impl.resolvable.call;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTOperatorCallImplTest extends ODTTestCase {

    @Test
    void testResolveOperatorCall() {
        String content = "DEFINE QUERY query => 'string value';\n" +
                "DEFINE QUERY <caret>callingQuery => query;\n";
        assertContainsElements(resolveQueryAtCaret(content), OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testResolveOperatorCallWithContext() {
        String content = "DEFINE QUERY query($param) => $param;\n" +
                "DEFINE QUERY <caret>callingQuery => query('test');\n";
        assertContainsElements(resolveQueryAtCaret(content), OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testResolveOperatorCallWithInput() {
        Assertions.assertEquals(OntologyModelConstants.getXsdStringInstance(), resolveQueryStatementToSingleResult("'test' / FIRST"));
    }

    @Test
    void testResolveOperatorCallWithCallContent() {
        Assertions.assertEquals(OntologyModelConstants.getXsdBooleanInstance(), resolveQueryStatementToSingleResult("'test' / MAP(true)"));
    }
}
