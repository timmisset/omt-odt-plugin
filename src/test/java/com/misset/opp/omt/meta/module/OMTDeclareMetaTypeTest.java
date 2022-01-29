package com.misset.opp.omt.meta.module;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTDeclareMetaTypeTest extends OMTCompletionTestCase {

    @Test
    void testShowsSuggestionForDeclared() {
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule");
        myFixture.addFileToProject("another.module.omt", "moduleName: AnotherModule");
        configureByText("current.interface.omt",
                "<caret>\n");
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "declare");
    }

    @Test
    void testShowsCompletionsForModules() {
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule");
        myFixture.addFileToProject("another.module.omt", "moduleName: AnotherModule");
        configureByText("current.interface.omt",
                "declare:\n" +
                        "   <caret>");
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "SomeModule", "AnotherModule");
        assertDoesntContain(lookupStrings, "CurrentModule");
    }

    @Test
    void testHasReferenceToModule() {
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule\n" +
                "exports:\n" +
                "- memberA\n" +
                "- memberB\n");
        configureByText("current.interface.omt",
                "declare:\n" +
                        "   <caret>SomeModule:\n" +
                        "");
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof OMTFile);
        });
    }

    @Test
    void testCanFindUsageOfExportedMember() {
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule\n" +
                "exports:\n" +
                "- memberA\n" +
                "- memberB\n");
        configureByText("current.interface.omt",
                "declare:\n" +
                        "   <caret>SomeModule:\n" +
                        "");
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof OMTFile);
        });
    }

}
