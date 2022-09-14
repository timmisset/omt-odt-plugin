package com.misset.opp.odt.psi.impl.resolvable.call;

import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTOperatorCallImplTest extends ODTTestCase {

    @Test
    void testResolveOperatorCall() {
        String content = "DEFINE QUERY query => 'string value';\n" +
                "DEFINE QUERY <caret>callingQuery => query;\n";
        assertContainsElements(resolveQueryAtCaret(content), OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testResolveOperatorCallWithContext() {
        String content = "DEFINE QUERY query($param) => $param;\n" +
                "DEFINE QUERY <caret>callingQuery => query('test');\n";
        assertContainsElements(resolveQueryAtCaret(content), OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testResolveOperatorCallWithInput() {
        Assertions.assertEquals(OppModelConstants.getXsdStringInstance(), resolveQueryStatementToSingleResult("'test' / FIRST"));
    }

    @Test
    void testResolveOperatorCallWithCallContent() {
        Assertions.assertEquals(OppModelConstants.getXsdBooleanInstance(), resolveQueryStatementToSingleResult("'test' / MAP(true)"));
    }
}
