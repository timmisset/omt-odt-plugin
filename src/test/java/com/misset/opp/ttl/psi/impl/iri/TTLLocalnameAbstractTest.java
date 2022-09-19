package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.ttl.psi.TTLLocalname;
import com.misset.opp.ttl.testcase.TTLTestCase;
import org.junit.jupiter.api.Test;

class TTLLocalnameAbstractTest extends TTLTestCase {

    @Test
    void testAcceptsNewName() {
        String content = "ont:ClassName a ont:localname.";
        configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            TTLLocalname localname = myFixture.findElementByText("localname", TTLLocalname.class);
            localname.setName("newName");
            assertEquals("ont:ClassName a ont:newName.", getFile().getText());
        });
    }

    @Test
    void testCalculatesQualifiedUri() {
        String content = withPrefixes("ont:ClassName a ont:localname.");
        configureByText(content);
        ReadAction.run(() -> {
            TTLLocalname localname = myFixture.findElementByText("localname", TTLLocalname.class);
            assertEquals(testPrefixes.get("ont") + "localname", localname.getQualifiedIri());
        });
    }

}
