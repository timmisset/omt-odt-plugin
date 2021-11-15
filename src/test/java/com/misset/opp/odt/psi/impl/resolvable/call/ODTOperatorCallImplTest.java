package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.callable.builtin.operators.BuiltInOperator;
import com.misset.opp.omt.psi.impl.OMTCallable;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.OppModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTOperatorCallImplTest extends OMTOntologyTestCase {

    @Test
    void testGetCallableBuiltinOperator() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @LOG(<caret>CURRENT_DATETIME);\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            final ODTOperatorCallImpl operatorCall = myFixture.findElementByText("CURRENT_DATETIME",
                    ODTOperatorCallImpl.class);
            Assertions.assertTrue(operatorCall.getCallable() instanceof BuiltInOperator);
        });
    }

    @Test
    void testGetCallableStandaloneQuery() {
        String content = "model: \n" +
                "   StandaloneQuery: !StandaloneQuery\n" +
                "       query:\n" +
                "           'hello'\n" +
                "\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           @LOG(<caret>StandaloneQuery);";
        configureByText(content);
        ReadAction.run(() -> {
            final ODTOperatorCallImpl operatorCall = myFixture.findElementByText("StandaloneQuery",
                    ODTOperatorCallImpl.class);
            Assertions.assertTrue(operatorCall.getCallable() instanceof OMTCallable);
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

    @Test
    void testResolveOperatorCallWithContext() {
        String content = insideActivityWithPrefixes("queries: |\n" +
                "   DEFINE QUERY query($param) => $param;\n" +
                "   DEFINE QUERY <caret>callingQuery => query('test');\n");
        assertContainsElements(resolveQueryAtCaret(content),
                OppModel.INSTANCE.XSD_STRING_INSTANCE);
    }
}
