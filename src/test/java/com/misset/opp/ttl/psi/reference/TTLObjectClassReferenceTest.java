package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.testcase.TTLTestCase;
import org.junit.jupiter.api.Test;

class TTLObjectClassReferenceTest extends TTLTestCase {

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
            assertTrue(elementAtCaret instanceof TTLSubject);
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
            assertTrue(elementAtCaret instanceof TTLSubject);
            assertEquals("ont:ClassName", elementAtCaret.getText());
        });
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
            assertTrue(elementAtCaret instanceof TTLSubject);
            assertEquals("ont:ClassName", elementAtCaret.getText());
        });
    }

}
