package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.ttl.util.TTLValidationUtil.ERROR_MESSAGE_BOOLEAN;

class ODTCodeInspectionBooleanTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCodeInspectionBoolean.class);
    }

    @Test
    void testHasErrorInIfBlock() {
        configureByText(insideProcedureRunWithPrefixes("IF 12 { @LOG('hi') }"));
        assertHasError(ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorInIfBlock() {
        configureByText(insideProcedureRunWithPrefixes("IF true { @LOG('hi') }"));
        assertNoError(ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasErrorInBooleanStatement() {
        configureByText(insideProcedureRunWithPrefixes("12 AND true"));
        assertHasError(ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorInBooleanStatement() {
        configureByText(insideProcedureRunWithPrefixes("false AND true"));
        assertNoError(ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorInBooleanStatementWithEquation() {
        configureByText(insideProcedureRunWithPrefixes("false AND 1 == 1"));
        assertNoError(ERROR_MESSAGE_BOOLEAN);
    }

}
