package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTUnnecessaryWrappingInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnnecessaryWrappingInspection.class);
    }

    @Test
    void testHasWarningWhenUnnecessaryWrapping() {
        String content = "import:\n" +
                "   './test.omt':\n" +
                "   - test\n" +
                "";
        configureByText(content);
        assertHasWarning(OMTUnnecessaryWrappingInspection.UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT);

        invokeQuickFixIntention(OMTUnnecessaryWrappingInspection.UNWRAP_LOCAL_QUICKFIX_FAMILY_NAME);
        assertNoWarning(OMTUnnecessaryWrappingInspection.UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT);
    }

    @Test
    void testHasNoWarningWhenNoUnnecessaryWrapping() {
        String content = "import:\n" +
                "   '@client/test.omt':\n" +
                "   - test\n" +
                "";
        configureByText(content);
        assertNoWarning(OMTUnnecessaryWrappingInspection.UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT);
    }
    @Test
    void testHasNoWarningWhenNotWrapped() {
        String content = "import:\n" +
                "   ./test.omt:\n" +
                "   - test\n" +
                "";
        configureByText(content);
        assertNoWarning(OMTUnnecessaryWrappingInspection.UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT);
    }
}
