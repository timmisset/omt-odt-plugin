package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.Variable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTUsageVariableDelegateTest extends ODTTestCase {

    @Test
    void testGetTypeFromExternalVariable() {
        String content = "$<caret>variable";
        ODTFileTestImpl odtFileTest = configureByText(content);

        Variable variable = mock(Variable.class);
        doReturn("$variable").when(variable).getName();
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(variable).resolve();
        odtFileTest.addVariable(variable);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OntologyModelConstants.getXsdStringInstance());
        });
    }

    @Test
    void testGetTypeFromLocalVariableFromCallableValue() {
        configureByText("@FOREACH('test', $<caret>value);");
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OntologyModelConstants.getXsdStringInstance());
        });
    }

    @Test
    void testGetTypeFromLocalVariableFromCallableIndex() {
        configureByText("@FOREACH('test', $<caret>index);");
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OntologyModelConstants.getXsdIntegerInstance());
        });
    }

    @Test
    void testGetTypeFromAssignment() {
        String content = "VAR $values = 'test';\n" +
                "@FOREACH($values, $<caret>value);";
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OntologyModelConstants.getXsdStringInstance());
        });
    }

    @Test
    void testGetTypeFromMultipleAssignments() {
        String content = "VAR $values = 'test';\n" +
                "@FOREACH($values, $<caret>value);";
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OntologyModelConstants.getXsdStringInstance());
        });
    }

    @Test
    void testGetTypeReassignment() {
        // this test's actual purpose is to validate that there is no stackoverflow
        // caused by the value trying to resolve itself by the not yet resolved assignment
        String content = "VAR $total = 1;\n" +
                "$total = $<caret>total / PLUS(1);";
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            assertContainsElements(((ODTVariable) elementAtCaret).resolve(), OntologyModelConstants.getXsdIntegerInstance());
        });
    }
}
