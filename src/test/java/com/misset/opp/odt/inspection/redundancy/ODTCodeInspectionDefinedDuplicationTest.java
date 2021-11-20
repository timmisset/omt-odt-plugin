package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.ODTCodeInspectionDefinedDuplication.WARNING_MESSAGE_DUPLICATION;
import static com.misset.opp.odt.inspection.ODTCodeInspectionDefinedDuplication.WARNING_MESSAGE_SHADOW;

class ODTCodeInspectionDefinedDuplicationTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(com.misset.opp.odt.inspection.ODTCodeInspectionDefinedDuplication.class);
    }

    @Test
    void testHasDuplicationWarningInFile() {
        String content = "queries: |\n" +
                "   DEFINE QUERY query => 'a';\n" +
                "   DEFINE QUERY query => 'b';";
        configureByText(content);
        assertHasWarning(WARNING_MESSAGE_DUPLICATION);
    }

    @Test
    void testHasNoDuplicationWarningOnDifferentNamesInFile() {
        String content = "queries: |\n" +
                "   DEFINE QUERY queryA => 'a';\n" +
                "   DEFINE QUERY queryB => 'b';";
        configureByText(content);
        assertNoWarning(WARNING_MESSAGE_DUPLICATION);
    }

    @Test
    void testHasNoDuplicationWarningInFile() {
        String content = "queries: |\n" +
                "   DEFINE QUERY sameName => 'a';\n" +
                "commands: |\n" +
                "   DEFINE COMMAND sameName => { }";
        configureByText(content);
        assertNoWarning(WARNING_MESSAGE_DUPLICATION);
    }

    @Test
    void testHasShadowWarningInOMTFile() {
        String content = "commands: |\n" +
                "   DEFINE COMMAND sameName => { }\n" +
                "\n" +
                "model:\n" +
                "   sameName: !Procedure\n" +
                "       onRun: @LOG('hi');\n" +
                "";
        configureByText(content);
        assertHasWarning(WARNING_MESSAGE_SHADOW);
    }

}
