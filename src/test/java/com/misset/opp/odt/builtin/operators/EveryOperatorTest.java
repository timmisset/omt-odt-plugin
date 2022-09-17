package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EveryOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EveryOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testValidInputs() {
        assertValidInput(EveryOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
        assertInvalidInput(EveryOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(EveryOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertInvalidArgument(EveryOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("EVERY", EveryOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, EveryOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, EveryOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(EveryOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentType(EveryOperator.INSTANCE, 1, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(EveryOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }
}
