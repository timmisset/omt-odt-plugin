package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EveryOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EveryOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testValidInputs() {
        assertValidInput(EveryOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance());
        assertInvalidInput(EveryOperator.INSTANCE, OppModelConstants.getXsdIntegerInstance(), TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(EveryOperator.INSTANCE, 0, OppModelConstants.getXsdBooleanInstance());
        assertInvalidArgument(EveryOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance(), TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
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
        assertGetAcceptableArgumentType(EveryOperator.INSTANCE, 0, OppModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentType(EveryOperator.INSTANCE, 1, OppModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(EveryOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance());
    }
}
