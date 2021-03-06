package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTVariableReferenceTest extends OMTTestCase {

    @Test
    void testODTReferenceCanResolve() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variable = 'test';\n" +
                        "@LOG($<caret>variable)"
        );
        configureByText(content);
        assertResolvableReference();
    }

    @Test
    void testODTReferenceDefineParam() {
        String content = "DEFINE QUERY query($param) => $pa<caret>ram;";
        myFixture.configureByText("test.odt", content);

        assertResolvableReference();
    }

    @Test
    void testODTReferenceCannotResolveWrongName() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variableA = 'test';\n" +
                        "@LOG($<caret>variableB)"
        );
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTReferenceCannotResolveWrongOrder() {
        String content = insideProcedureRunWithPrefixes(
                "@LOG($<caret>variable)\n" +
                        "VAR $variable = 'test';\n"
        );
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTAssignmentReferenceCannotResolve() {
        String content = insideProcedureRunWithPrefixes(
                "<caret>$variableA = 'test';"
        );
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTReferenceCannotResolveFromDifferentDEFINE() {
        String content = insideProcedureRunWithPrefixes(
                "DEFINE COMMAND command($variable) => { @LOG($variable); }\n" +
                        "DEFINE COMMAND anotherCommand => { @LOG($<caret>variable); }"
        );
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTReferenceCanResolveFromParentBlockContainer() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variable = 12;\n" +
                        "IF true { @LOG($<caret>variable); }"
        );
        configureByText(content);
        assertResolvableReference();
    }

    @Test
    void testODTReferenceCannotResolveFromDifferentBlockContainer() {
        String content = insideProcedureRunWithPrefixes(
                "IF true { VAR $variable = 12; } ELSE { @LOG($<caret>variable); }"
        );
        configureByText(content);
        assertNoResolvableReference();
    }

    @Test
    void testODTReferenceToOMTActivityVariable() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $test\n" +
                        "payload:\n" +
                        "   test: $<caret>test\n"
        );
        configureByText(content);
        ReadAction.run(() -> {
            // the getElementAtCaret method returns the result of resolving the reference of the element at the caret
            // in this case, it should return OMT variable: $test declared in the variables block
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is resolved to the OMT variable
            Assertions.assertTrue(elementAtCaret instanceof YAMLPlainTextImpl);
        });
    }

    @Test
    void testODTReferenceToOMTComponentBindingParameter() {
        String content = insideComponentWithPrefixes(
                "bindings:\n" +
                        "   test: $test\n" +
                        "payload:\n" +
                        "   test: $<caret>test\n"
        );
        configureByText(content);
        ReadAction.run(() -> {
            // the getElementAtCaret method returns the result of resolving the reference of the element at the caret
            // in this case, it should return OMT variable: $test declared in the variables block
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is resolved to the OMT variable
            Assertions.assertTrue(elementAtCaret instanceof YAMLPlainTextImpl);
        });
    }

    @Test
    void testODTReferenceToOMTComponentDestructedBindingParameter() {
        String content = insideComponentWithPrefixes(
                "bindings:\n" +
                        "   test:\n" +
                        "       bindTo: $test\n" +
                        "payload:\n" +
                        "   test: $<caret>test\n"
        );
        configureByText(content);
        ReadAction.run(() -> {
            // the getElementAtCaret method returns the result of resolving the reference of the element at the caret
            // in this case, it should return OMT variable: $test declared in the variables block
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is resolved to the OMT variable
            Assertions.assertTrue(elementAtCaret instanceof YAMLPlainTextImpl);
        });
    }

    @Test
    void testODTAssignmentValueReferenceToOMT() {
        String content = insideActivityWithPrefixes(
                "params:\n" +
                        "- $te<caret>st\n" +
                        "variables:\n" +
                        "- $another = $test\n"
        );
        configureByText(content);
        underProgress(() -> ReadAction.run(() ->
                Assertions.assertEquals(1,
                        ReferencesSearch.search(myFixture.findElementByText("$test", YAMLPlainTextImpl.class))
                                .findAll()
                                .size())));

    }

    @Test
    void testODTReferenceToShadowed() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $test\n" + // <-- wrapped in an ODTQueryStep
                        "onStart: |\n" +
                        "   VAR $test = 'hello';\n" + //<-- wrapped in ODTVariableAssignment, use for assertion
                        "   @LOG($<caret>test);\n"
        );
        configureByText(content);
        ReadAction.run(() -> {
            // the getElementAtCaret method returns the result of resolving the reference of the element at the caret
            // in this case, it should return OMT variable: $test declared in the variables block
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is resolved to the OMT variable
            Assertions.assertTrue(elementAtCaret instanceof ODTVariable);
            Assertions.assertTrue(elementAtCaret.getParent() instanceof ODTVariableAssignment);
        });
    }

    @Test
    void testRefactorRename() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $test\n" +
                        "payload:\n" +
                        "   test: $<caret>test\n"
        );
        final OMTFile omtFile = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("$newName");
            Assertions.assertEquals(insideActivityWithPrefixes(
                    "variables:\n" +
                            "- $newName\n" +
                            "payload:\n" +
                            "   test: $newName\n"), omtFile.getText());
        });
    }

    @Test
    void testRefactorRenameAddsRequiredPrefix() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $test\n" +
                        "payload:\n" +
                        "   test: $<caret>test\n"
        );
        final OMTFile omtFile = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("newName");
            Assertions.assertEquals(insideActivityWithPrefixes(
                    "variables:\n" +
                            "- $newName\n" +
                            "payload:\n" +
                            "   test: $newName\n"), omtFile.getText());
        });
    }

    @Test
    void testRefactorRenameODTVariable() {
        String content = "queries:\n" +
                "  DEFINE QUERY query($<caret>param) => $param;";
        OMTFile omtFile = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> myFixture.renameElementAtCaret("$newName"));
        String contentAfterRename = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals("queries:\n" +
                "  DEFINE QUERY query($newName) => $newName;", contentAfterRename);
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
