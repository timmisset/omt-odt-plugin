package com.misset.opp.odt.inspection.calls.commands;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.calls.commands.ODTCommandInspectionForEach.ALL_VALUES_ARE_TREATED_EQUAL;
import static com.misset.opp.odt.inspection.calls.commands.ODTCommandInspectionForEach.CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH;

class ODTCommandInspectionForEachTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCommandInspectionForEach.class);
    }

    @Test
    void testForEachCommandCannotCallProcedureInsideForEachError() {
        String content = insideProcedureRunWithPrefixes("" +
                "@FOREACH('', { @Procedure(); });");
        configureByText(content);
        assertHasError(CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH);
    }

    @Test
    void testForEachCommandCannotCallProcedureInsideForEachErrorNoErrors() {
        String content = insideProcedureRunWithPrefixes("" +
                "@FOREACH('', { @LOG(''); });");
        configureByText(content);
        assertNoError(CANNOT_CALL_PROCEDURE_INSIDE_FOR_EACH);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsWarning() {
        String content = insideProcedureRunWithPrefixes("" +
                "@FOREACH('', { $value = ''; });");
        configureByText(content);
        assertHasWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsWarningWhenUpdatingCollection() {
        String content = insideProcedureRunWithPrefixes("" +
                "VAR $someItem;\n" +
                "@FOREACH('', { $someItem / ont:someProperty += $value });");
        configureByText(content);
        assertHasWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsNoWarningWhenInsideLogicalBlock() {
        String content = insideProcedureRunWithPrefixes("" +
                "@FOREACH('', { IF 1 == 2 { $value = ''; } });");
        configureByText(content);
        assertNoWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsNoWarningWhenInsideCall() {
        String content = insideProcedureRunWithPrefixes("" +
                "@FOREACH('', { @SomeCommand($value); });");
        configureByText(content);
        assertNoWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }

    @Test
    void testForEachCommandUnnecessaryUsageOfForEachShowsNoWarningWhenUsedAsAssignmentValue() {
        String content = insideProcedureRunWithPrefixes("" +
                "VAR $myValue;\n" +
                "@FOREACH('', { $myValue = $value; });");
        configureByText(content);
        assertNoWarning(ALL_VALUES_ARE_TREATED_EQUAL);
    }
}
