package com.misset.opp.omt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTDuplicateImportInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTDuplicateImportInspection.class);
    }

    @Test
    void testHasDuplicationWarningForMembers() {
        String content = "import:\n" +
                "   file.omt:\n" +
                "   - memberA\n" +
                "   - memberA";
        configureByText(content);
        assertHasWarning("Duplication");
    }

    @Test
    void testHasDuplicationWarningForPaths() {
        String content = "import:\n" +
                "   file.omt:\n" +
                "   - memberA\n" +
                "   file.omt:\n" +
                "   - memberB";
        configureByText(content);
        assertHasWarning("Duplication");
    }

    @Test
    void testHasDuplicationWarningForPathsWithDifferentAccessMethods() {
        String content = "import:\n" +
                "   '@client/activiteit-domain/file.omt':\n" +
                "   - memberA\n" +
                "   '@activiteit/file.omt':\n" +
                "   - memberB";
        configureByText(content);
        assertHasWarning("Duplication");
    }

}
