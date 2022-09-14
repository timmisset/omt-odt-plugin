package com.misset.opp.omt.inspection.redundancy;

import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTDuplicateImportInspectionTest extends OMTTestCase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTDuplicateImportInspection.class));
    }

    @Test
    void testHasDuplicationWarningForMembers() {
        String content = "import:\n" +
                "   file.omt:\n" +
                "   - memberA\n" +
                "   - memberA";
        configureByText(content);
        inspection.assertHasWarning("Duplication");
    }

    @Test
    void testHasDuplicationWarningForPaths() {
        String content = "import:\n" +
                "   file.omt:\n" +
                "   - memberA\n" +
                "   file.omt:\n" +
                "   - memberB";
        configureByText(content);
        inspection.assertHasWarning("Duplication");
    }

    @Test
    void testHasDuplicationWarningForPathsWithDifferentAccessMethods() {
        String content = "import:\n" +
                "   '@client/activiteit-domain/file.omt':\n" +
                "   - memberA\n" +
                "   '@activiteit/file.omt':\n" +
                "   - memberB";
        configureByText(content);
        inspection.assertHasWarning("Duplication");
    }

}
