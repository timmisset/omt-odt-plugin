package com.misset.opp.omt.inspection;

import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTUnnecessaryWrappingInspectionTest extends OMTTestCase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTUnnecessaryWrappingInspection.class));
    }

    @Test
    void testHasWarningWhenUnnecessaryWrapping() {
        String content = "import:\n" +
                "   './test.omt':\n" +
                "   - test\n" +
                "";
        configureByText(content);
        inspection.assertHasWarning(OMTUnnecessaryWrappingInspection.UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT);

        inspection.invokeQuickFixIntention(OMTUnnecessaryWrappingInspection.UNWRAP_LOCAL_QUICKFIX_FAMILY_NAME);
        inspection.assertNoWarning(OMTUnnecessaryWrappingInspection.UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT);
    }

    @Test
    void testHasNoWarningWhenNoUnnecessaryWrapping() {
        String content = "import:\n" +
                "   '@client/test.omt':\n" +
                "   - test\n" +
                "";
        configureByText(content);
        inspection.assertNoWarning(OMTUnnecessaryWrappingInspection.UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT);
    }
    @Test
    void testHasNoWarningWhenNotWrapped() {
        String content = "import:\n" +
                "   ./test.omt:\n" +
                "   - test\n" +
                "";
        configureByText(content);
        inspection.assertNoWarning(OMTUnnecessaryWrappingInspection.UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT);
    }
}
