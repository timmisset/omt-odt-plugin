package com.misset.opp.odt.inspection.calls;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTCallInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCallInspection.class);
    }

    @Test
    void testIllegalFlagToActivity() {
        String content = "" +
                "model:\n" +
                "   Activity: !Activity\n" +
                "       onStart:\n" +
                "           @LOG('hi');\n" +
                "   AnotherActivity: !Activity\n" +
                "       onStart:\n" +
                "           @Activity!wrongFlag();\n";
        configureByText(content);
        assertHasError("Illegal flag");
    }

    @Test
    void testValidFlagToActivity() {
        String content = "" +
                "model:\n" +
                "   Activity: !Activity\n" +
                "       onStart:\n" +
                "           @LOG('hi');\n" +
                "   AnotherActivity: !Activity\n" +
                "       onStart:\n" +
                "           @Activity!nested();\n";
        configureByText(content);
        assertNoError("Illegal flag");
    }
}