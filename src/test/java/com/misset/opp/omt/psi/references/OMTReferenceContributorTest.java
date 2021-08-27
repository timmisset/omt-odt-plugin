package com.misset.opp.omt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OMTReferenceContributorTest extends OMTTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void testHasModelItemReference() {
        String content = "model:\n" +
                "   <caret>Activiteit: !Activity";
        configureByText(content);
        ReadAction.run(() -> {
            final YAMLKeyValue keyValue = (YAMLKeyValue) myFixture.getElementAtCaret();
            final PsiReference[] references = keyValue.getReferences();
            assertTrue(references.length == 0);
        });
    }
}
