package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.ODTQualifiedURIInspection.WEAK_WARNING;

class ODTQualifiedURIInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTQualifiedURIInspection.class);
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.addFileToProject("dummy.omt", "prefixes:\n" +
                "   ont: <http://ontology#>");
    }

    @Test
    void testHasWarning() {
        String content = "queries:\n" +
                "   DEFINE QUERY query => <http://ontology#ClassA>";
        configureByText(content);
        assertHasWarning(WEAK_WARNING);
    }

    @Test
    void testReplacesUriWithCurie() {
        String content = "queries:\n" +
                "   DEFINE QUERY query => <http://ontology#ClassA>";
        configureByText(content);
        assertHasWarning(WEAK_WARNING);
        invokeQuickFixIntention(getAllQuickFixes().get(0));
        ReadAction.run(() -> {
            String text = getFile().getText();
            Assertions.assertEquals("queries:\n" +
                    "  DEFINE QUERY query => ont:ClassA\n" +
                    "prefixes:\n" +
                    "  ont: <http://ontology#>", text);
        });
    }
}