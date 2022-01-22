package com.misset.opp.odt.inspection.resolvable;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTIgnoredReturnValueInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTIgnoredReturnValueInspection.class);
    }

    @Test
    void testHasWarningWhenIgnoredQuery() {
        String content = insideProcedureRunWithPrefixes("true;");
        configureByText(content);
        assertHasWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasWarningWhenIgnoredCommandCall() {
        String content = insideProcedureRunWithPrefixes("" +
                "DEFINE COMMAND command => { RETURN true; }\n" +
                "@command();");
        configureByText(content);
        assertHasWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasNoWarningWhenCommandCallWithoutReturn() {
        String content = insideProcedureRunWithPrefixes("" +
                "DEFINE COMMAND command => { @LOG('test'); }\n" +
                "@command();");
        configureByText(content);
        assertNoWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasNoWarningForVoidCommandCall() {
        String content = insideProcedureRunWithPrefixes("@LOG('test');");
        configureByText(content);
        assertNoWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasNoWarningForSimpleInjectableCommandCall() {
        String content = insideActivityWithPrefixes("" +
                "watchers:\n" +
                "   - query: true;");
        configureByText(content);
        assertNoWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasNoWarningForVariableAssignment() {
        String content = insideProcedureRunWithPrefixes("VAR $x = 12;");
        configureByText(content);
        assertNoWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testHasWarningForActivityWithReturnValue() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       returns: true\n" +
                "   Procedure: !Procedure\n" +
                "       onRun: |\n" +
                "           @Activity();";
        configureByText(content);
        assertHasWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }

    @Test
    void testNoWarningForActivityWithoutReturnValue() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       onStart: |\n" +
                "           @LOG(true);\n" +
                "   Procedure: !Procedure\n" +
                "       onRun: |\n" +
                "           @Activity();";
        configureByText(content);
        assertNoWeakWarning(ODTIgnoredReturnValueInspection.RESULT_IS_IGNORED);
    }
}
