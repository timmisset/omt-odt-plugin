package com.misset.opp.omt.meta.module;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.references.OMTDeclaredInterfaceReference;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.testCase.OMTCompletionTestCase;
import org.jetbrains.yaml.psi.YAMLValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class OMTDeclaredInterfaceMetaTypeTest extends OMTCompletionTestCase {
    @Test
    void testShowsCompletionsForExportedMembers() {
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule\n" +
                "export:\n" +
                "- memberA\n" +
                "- memberB\n");
        configureByText("current.interface.omt",
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
        configureByText("current.interface.omt",
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
        configureByText("current.interface.omt",
                "declare:\n" +
                        "   SomeModule:\n" +
                        "       <caret>memberA:\n" +
                        "");
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            ResolveResult[] resolveResults = ((OMTDeclaredInterfaceReference) elementAtCaret.getReference()).multiResolve(false);
            Assertions.assertEquals(2, resolveResults.length);
            Assertions.assertTrue(Arrays.stream(resolveResults).anyMatch(resolveResult -> resolveResult.getElement() instanceof YAMLValue));
            Assertions.assertTrue(Arrays.stream(resolveResults).anyMatch(resolveResult -> resolveResult.getElement() instanceof PsiCallable));
        });
    }

    @Test
    void testExportsMember() {
        myFixture.addFileToProject("exportingFile.omt", "queries:\n" +
                "   DEFINE QUERY memberA => '';");
        myFixture.addFileToProject("some.module.omt", "moduleName: SomeModule\n" +
                "import:\n" +
                "   ./exportingFile.omt:\n" +
                "   - memberA\n" +
                "export:\n" +
                "- memberA\n" +
                "- memberB\n");
        OMTFile interfaceFile = (OMTFile) myFixture.addFileToProject("current.interface.omt",
                "declare:\n" +
                        "   SomeModule:\n" +
                        "       memberA:\n" +
                        "");
        myFixture.configureByText("importingFile.omt", "import:\n" +
                "   ./current.interface.omt:\n" +
                "   - memberA\n" +
                "\n" +
                "queries:\n" +
                "   DEFINE QUERY query => <caret>memberA;\n");
        ReadAction.run(() -> {
            HashMap<String, List<PsiCallable>> exportingMembersMap = interfaceFile.getExportingMembersMap();
            Assertions.assertTrue(exportingMembersMap.containsKey("memberA"));

            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof PsiCallable);
        });
    }
}
