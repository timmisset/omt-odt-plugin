package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.type.ODTCodeInspectionBoolean.ERROR_MESSAGE;

class ODTCodeInspectionBooleanTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCodeInspectionBoolean.class);
    }

    @Test
    void testHasErrorInIfBlock() {
        configureByText(insideProcedureRunWithPrefixes("IF 12 { @LOG('hi') }"));
        assertHasError(ERROR_MESSAGE);
    }

    @Test
    void testHasNoErrorInIfBlock() {
        configureByText(insideProcedureRunWithPrefixes("IF true { @LOG('hi') }"));
        assertNoError(ERROR_MESSAGE);
    }

    @Test
    void testHasErrorInBooleanStatement() {
        configureByText(insideProcedureRunWithPrefixes("12 AND true"));
        assertHasError(ERROR_MESSAGE);
    }

    @Test
    void testHasNoErrorInBooleanStatement() {
        configureByText(insideProcedureRunWithPrefixes("false AND true"));
        assertNoError(ERROR_MESSAGE);
    }

    @Test
    void testHasNoErrorInBooleanStatementWithEquation() {
        configureByText(insideProcedureRunWithPrefixes("false AND 1 == 1"));
        assertNoError(ERROR_MESSAGE);
    }

}
