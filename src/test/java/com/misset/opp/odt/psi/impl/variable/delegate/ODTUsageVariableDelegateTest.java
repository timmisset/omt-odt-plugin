package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTUsageVariableDelegateTest extends OMTOntologyTestCase {

    @Test
    void testGetTypeFromLocalVariableNewValue() {
        String content = insideActivityWithPrefixes(
                "payload:\n" +
                        "   item:\n" +
                        "       value: 'test'\n" +
                        "       onChange: $<caret>newValue"
        );
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OppModel.INSTANCE.XSD_STRING_INSTANCE);
        });
    }

    @Test
    void testGetTypeFromLocalVariableOldValue() {
        String content = insideActivityWithPrefixes(
                "payload:\n" +
                        "   item:\n" +
                        "       value: 'test'\n" +
                        "       onChange: $<caret>oldValue"
        );
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OppModel.INSTANCE.XSD_STRING_INSTANCE);
        });
    }

    @Test
    void testGetTypeFromLocalVariableFromCallableValue() {
        String content = insideProcedureRunWithPrefixes("@FOREACH('test', $<caret>value);");
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OppModel.INSTANCE.XSD_STRING_INSTANCE);
        });
    }

    @Test
    void testGetTypeFromLocalVariableFromCallableIndex() {
        String content = insideProcedureRunWithPrefixes("@FOREACH('test', $<caret>index);");
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        });
    }

    @Test
    void testGetTypeFromAssignment() {
        String content = insideProcedureRunWithPrefixes("" +
                "VAR $values = 'test';\n" +
                "@FOREACH($values, $<caret>value);");
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OppModel.INSTANCE.XSD_STRING_INSTANCE);
        });
    }

    @Test
    void testGetTypeFromMultipleAssignments() {
        String content = insideProcedureRunWithPrefixes("" +
                "VAR $values = 'test';\n" +
                "@FOREACH($values, $<caret>value);");
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OppModel.INSTANCE.XSD_STRING_INSTANCE);
        });
    }

    @Test
    void testGetTypeReassignment() {
        // this test's actual purpose is to validate that there is no stackoverflow
        // caused by the the value trying to resolve itself by the not yet resolved assignment
        String content = insideProcedureRunWithPrefixes("" +
                "VAR $total = 1;\n" +
                "$total = $<caret>total / PLUS(1);");
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        });
    }

    @Test
    void testGetTypeFromGlobalVariableMedewerkerGraph() {
        assertEquals(OppModel.INSTANCE.MEDEWERKER_GRAPH, resolveQueryStatementToSingleResult("$medewerkerGraph"));
    }

    @Test
    void testGetTypeFromGlobalVariableUsername() {
        assertEquals(OppModel.INSTANCE.XSD_STRING_INSTANCE, resolveQueryStatementToSingleResult("$username"));
    }

    @Test
    void testGetTypeFromGlobalVariableOffline() {
        assertEquals(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE, resolveQueryStatementToSingleResult("$offline"));
    }

}
