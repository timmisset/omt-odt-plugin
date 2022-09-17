package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageInfo;
import com.misset.opp.ttl.psi.extend.TTLQualifiedIriResolver;
import com.misset.opp.ttl.testcase.TTLTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class TTLClassReferenceTest extends TTLTestCase {

    @Test
    void testHasReferenceToSuperclass() {
        String content = withPrefixes(
                "ont:SuperclassName\n" +
                        "  a owl:Class ;\n" +
                        "  a sh:NodeShape ;\n" +
                        ".\n" +
                        "ont:ClassName\n" +
                        "  a owl:Class ;\n" +
                        "  a sh:NodeShape ;\n" +
                        "  rdfs:subClassOf ont:Superclass<caret>Name ;\n" +
                        ".\n");
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertTrue(elementAtCaret instanceof TTLQualifiedIriResolver);
            assertEquals("ont:SuperclassName", elementAtCaret.getText());
        });
    }

    @Test
    void testHasReferenceToType() {
        String content = withPrefixes(
                "ont:ClassName\n" +
                        "  a owl:Class ;\n" +
                        "  a sh:NodeShape ;\n" +
                        ".\n" +
                        "ont:Individual\n" +
                        "  rdf:type ont:Class<caret>Name ;\n" +
                        ".\n");
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertTrue(elementAtCaret instanceof TTLQualifiedIriResolver);
            assertEquals("ont:ClassName", elementAtCaret.getText());
        });
    }

    @Test
    void testCanFindusage() {
        String content = withPrefixes(
                "ont:Class<caret>Name\n" +
                        "  a owl:Class ;\n" +
                        "  a sh:NodeShape ;\n" +
                        ".\n" +
                        "ont:Individual\n" +
                        "  rdf:type ont:ClassName ;\n" +
                        ".\n");
        configureByText(content);
        underProgress(() -> ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertTrue(elementAtCaret instanceof TTLQualifiedIriResolver);
            Collection<UsageInfo> usages = myFixture.findUsages(elementAtCaret);
            assertEquals(1, usages.size());
        }));
    }

    @Test
    void testCanFindUsageFromIriRef() {
        String content = withPrefixes(
                "<http://ontologie/<caret>ClassName>\n" +
                        "  a owl:Class ;\n" +
                        "  a sh:NodeShape ;\n" +
                        ".\n" +
                        "ont:Individual\n" +
                        "  rdf:type <http://ontologie/ClassName> ;\n" +
                        ".\n");
        configureByText(content);
        underProgress(() -> ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertTrue(elementAtCaret instanceof TTLQualifiedIriResolver);
            Collection<UsageInfo> usages = myFixture.findUsages(elementAtCaret);
            assertEquals(1, usages.size());
        }));
    }

    @Test
    void testHasReferenceToTypeAsA() {
        String content = withPrefixes(
                "ont:ClassName\n" +
                        "  a owl:Class ;\n" +
                        "  a sh:NodeShape ;\n" +
                        ".\n" +
                        "ont:Individual\n" +
                        "  a ont:Class<caret>Name ;\n" +
                        ".\n");
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertTrue(elementAtCaret instanceof TTLQualifiedIriResolver);
            assertEquals("ont:ClassName", elementAtCaret.getText());
        });
    }

}
