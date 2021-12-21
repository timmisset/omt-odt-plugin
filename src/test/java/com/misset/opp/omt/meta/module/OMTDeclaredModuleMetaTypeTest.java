package com.misset.opp.omt.meta.module;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.omt.psi.references.OMTModuleExportReference;
import com.misset.opp.testCase.OMTCompletionTestCase;
import org.jetbrains.yaml.psi.YAMLValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class OMTDeclaredModuleMetaTypeTest extends OMTCompletionTestCase {
    @Test
    void testShowsCompletionsForExportedMembers() {
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule\n" +
                "export:\n" +
                "- memberA\n" +
                "- memberB\n");
        configureByText("current.module.omt", "moduleName: CurrentModule\n" +
                "declare:\n" +
                "   SomeModule:\n" +
                "       <caret>");
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "memberA", "memberB");
    }

    @Test
    void testHasReferenceToExportedMember() {
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule\n" +
                "export:\n" +
                "- memberA\n" +
                "- memberB\n");
        configureByText("current.module.omt", "moduleName: CurrentModule\n" +
                "declare:\n" +
                "   SomeModule:\n" +
                "       <caret>memberA:\n" +
                "");
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLValue);
        });
    }

    @Test
    void testHasReferenceToExportedMemberAndDeclared() {
        myFixture.addFileToProject("exportingFile.omt", "queries:\n" +
                "   DEFINE QUERY memberA => '';");
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule\n" +
                "import:\n" +
                "   ./exportingFile.omt:\n" +
                "   - memberA\n" +
                "export:\n" +
                "- memberA\n" +
                "- memberB\n");
        configureByText("current.module.omt", "moduleName: CurrentModule\n" +
                "declare:\n" +
                "   SomeModule:\n" +
                "       <caret>memberA:\n" +
                "");
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            ResolveResult[] resolveResults = ((OMTModuleExportReference) elementAtCaret.getReference()).multiResolve(false);
            Assertions.assertEquals(2, resolveResults.length);
            Assertions.assertTrue(Arrays.stream(resolveResults).anyMatch(resolveResult -> resolveResult.getElement() instanceof YAMLValue));
            Assertions.assertTrue(Arrays.stream(resolveResults).anyMatch(resolveResult -> resolveResult.getElement() instanceof PsiCallable));
        });
    }
}