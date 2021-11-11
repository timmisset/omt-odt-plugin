package com.misset.opp.odt.psi.impl.call;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.callable.builtin.operators.BuiltInOperator;
import com.misset.opp.testCase.OntologyTestCase;
import com.misset.opp.ttl.OppModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTOperatorCallImplTest extends OntologyTestCase {

    @Test
    void testGetCallableBuiltinOperator() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @LOG(<caret>CURRENT_DATETIME);\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            final ODTOperatorCallImpl commit = myFixture.findElementByText("CURRENT_DATETIME", ODTOperatorCallImpl.class);
            Assertions.assertTrue(commit.getCallable() instanceof BuiltInOperator);
        });
    }

    @Test
    void testResolveOperatorCall() {
        String content = insideActivityWithPrefixes("queries: |\n" +
                "   DEFINE QUERY query => 'string value';\n" +
                "   DEFINE QUERY <caret>callingQuery => query;\n");
        assertContainsElements(resolveQueryAtCaret(content),
                OppModel.INSTANCE.XSD_STRING_INSTANCE);
    }
}
