package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.ttl.psi.TTLIriReference;
import com.misset.opp.ttl.testcase.TTLTestCase;
import org.junit.jupiter.api.Test;

class TTLIriReferenceAbstractTest extends TTLTestCase {

    @Test
    void testAcceptsNewName() {
        String content = "ont:ClassName a <http://ontology#localname>.";
        configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            TTLIriReference iriReference = myFixture.findElementByText("<http://ontology#localname>", TTLIriReference.class);
            iriReference.setName("newName");
            assertEquals("ont:ClassName a <http://ontology#newName>.", getFile().getText());
        });
    }

    @Test
    void testCalculatesQualifiedUri() {
        String content = withPrefixes("ont:ClassName a <http://ontology#localname>.");
        configureByText(content);
        ReadAction.run(() -> {
            TTLIriReference iriReference = myFixture.findElementByText("<http://ontology#localname>", TTLIriReference.class);
            assertEquals("http://ontology#localname", iriReference.getQualifiedIri());
        });
    }

}
