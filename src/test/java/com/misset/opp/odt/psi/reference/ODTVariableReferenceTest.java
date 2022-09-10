package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTVariableReferenceTest extends ODTTestCase {

    @Test
    void testODTReferenceCanResolve() {
        String content = "VAR $variable = 'test';\n" +
                "@LOG($<caret>variable)";
        configureByText(content);
        assertResolvableReference();
    }

    @Test
    void testODTReferenceDefineParam() {
        String content = "DEFINE QUERY query($param) => $pa<caret>ram;";
        configureByText(content);

        assertResolvableReference();
    }

    @Test
    void testODTReferenceCannotResolveWrongName() {
        String content = "VAR $variableA = 'test';\n" +
                "@LOG($<caret>variableB)";
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTReferenceCannotResolveWrongOrder() {
        String content = "@LOG($<caret>variable)\n" +
                "VAR $variable = 'test';\n";
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTAssignmentReferenceCannotResolve() {
        String content = "<caret>$variableA = 'test';";
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTReferenceCannotResolveFromDifferentDEFINE() {
        String content = "DEFINE COMMAND command($variable) => { @LOG($variable); }\n" +
                "DEFINE COMMAND anotherCommand => { @LOG($<caret>variable); }";
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTReferenceCanResolveFromParentBlockContainer() {
        String content = "VAR $variable = 12;\n" +
                "IF true { @LOG($<caret>variable); }";
        configureByText(content);
        assertResolvableReference();
    }

    @Test
    void testODTReferenceCannotResolveFromDifferentBlockContainer() {
        String content = "IF true { VAR $variable = 12; } ELSE { @LOG($<caret>variable); }";
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTReferenceToExternalVariable() {
        String content = "$variable";
        ODTFileTestImpl odtFileTest = configureByText(content);
        PsiVariable variable = mock(PsiVariable.class);
        doReturn("$variable").when(variable).getName();
        doReturn(variable).when(variable).getOriginalElement();
        odtFileTest.addVariable(variable);
        ReadAction.run(() -> {
            final ODTVariable odtVariable = myFixture.findElementByText("$variable", ODTVariable.class);
            assertEquals(variable, odtVariable.getReference().resolve());
        });
    }

    @Test
    void testRefactorRename() {
        String content = "VAR $variable; $<caret>variable;";
        final ODTFile odtFile = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("$newName");
            Assertions.assertEquals("VAR $newName; $newName;", odtFile.getText());
        });
    }

    @Test
    void testRefactorRenameAddsRequiredPrefix() {
        String content = "VAR $variable; $<caret>variable;";
        final ODTFile odtFile = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("newName");
            Assertions.assertEquals("VAR $newName; $newName;", odtFile.getText());
        });
    }

    @Test
    void testRefactorRenameInputParameter() {
        String content = "DEFINE QUERY query($<caret>param) => $param;";
        ODTFile omtFile = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> myFixture.renameElementAtCaret("$newName"));
        String contentAfterRename = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals("DEFINE QUERY query($newName) => $newName;", contentAfterRename);
    }

    private void assertNoResolvableReference() {
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is not resolved to the declared variable, returns the $variableB usage variable as named element
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            final PsiReference reference = elementAtCaret.getReference();
            Assertions.assertNotNull(reference);
            Assertions.assertNull(reference.resolve());
        });
    }

    private void assertResolvableReference() {
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is resolved to the declared variable
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            final ODTVariable variable = (ODTVariable) elementAtCaret;
            Assertions.assertTrue(variable.isDeclaredVariable());
        });
    }

}
