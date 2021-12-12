package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.builtin.commands.BuiltInCommand;
import com.misset.opp.callable.local.LocalCommand;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.testCase.OMTTestCase;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTCommandCallImplTest extends OMTTestCase {

    @Test
    void testGetCallableLocalCommand() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @<caret>COMMIT();\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(getCallByName("COMMIT").getCallable() instanceof LocalCommand));
    }

    @Test
    void testGetCallableBuiltinCommand() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @<caret>LOG('hello world');\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(getCallByName("LOG").getCallable() instanceof BuiltInCommand));
    }

    @Test
    void testGetSecondArgumentFromOMTCallable() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       onStart: @LOG('hi');\n" +
                "   Procedure: !Procedure\n" +
                "       onRun: |\n" +
                "           VAR $result, <caret>$committed = @Activity();";
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            Set<OntResource> variableType = ((ODTVariable) elementAtCaret).getType();
            assertContainsElements(variableType, OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
        });
    }

}
