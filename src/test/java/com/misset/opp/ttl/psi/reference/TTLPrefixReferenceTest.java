package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.misset.opp.testCase.TTLTestCase;
import com.misset.opp.ttl.psi.TTLPrefixId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class TTLPrefixReferenceTest extends TTLTestCase {

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
            Assertions.assertTrue(elementAtCaret instanceof TTLPrefixId);
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
        withProgress(() -> ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Collection<PsiReference> all = ReferencesSearch.search(elementAtCaret)
                    .findAll();
            Assertions.assertEquals(3, all.size());
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
        withProgress(() -> ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Collection<PsiReference> all = ReferencesSearch.search(elementAtCaret)
                    .filtering(psiReference -> psiReference.isReferenceTo(elementAtCaret))
                    .findAll();
            Assertions.assertEquals(3, all.size());
        }));
    }

}