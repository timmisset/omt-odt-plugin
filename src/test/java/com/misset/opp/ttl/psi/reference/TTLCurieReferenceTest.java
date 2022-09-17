package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageInfo;
import com.misset.opp.ttl.psi.TTLPrefix;
import com.misset.opp.ttl.testcase.TTLTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class TTLCurieReferenceTest extends TTLTestCase {

    @Test
    void testHasReferenceToPrefixId() {
        String content = "" +
                "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
                "<http://ontology>\n" +
                "  a o<caret>wl:Ontology ;\n" +
                "  owl:imports <http://ontologie.politie.nl/def/ontologie> ;\n" +
                "  owl:versionInfo \"Created manually\" ;\n" +
                ".";
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof TTLPrefix);
        });
    }

    @Test
    void testCanFindUsages() {
        String content = "" +
                "@prefix o<caret>wl: <http://www.w3.org/2002/07/owl#> .\n" +
                "<http://ontology>\n" +
                "  a owl:Ontology ;\n" +
                "  owl:imports <http://ontologie.politie.nl/def/ontologie> ;\n" +
                "  owl:versionInfo \"Created manually\" ;\n" +
                ".";
        configureByText(content);
        underProgress(() -> ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Collection<UsageInfo> usages = myFixture.findUsages(elementAtCaret);
            Assertions.assertEquals(3, usages.size());
        }));
    }

    @Test
    void testCanFindUnnamedUsages() {
        String content = "" +
                "@prefix <caret>: <http://www.w3.org/2002/07/owl#> .\n" +
                "<http://ontology>\n" +
                "  a :Ontology ;\n" +
                "  :imports <http://ontologie.politie.nl/def/ontologie> ;\n" +
                "  :versionInfo \"Created manually\" ;\n" +
                ".";
        configureByText(content);
        underProgress(() -> ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Collection<UsageInfo> usages = myFixture.findUsages(elementAtCaret);
            Assertions.assertEquals(3, usages.size());
        }));
    }

}
