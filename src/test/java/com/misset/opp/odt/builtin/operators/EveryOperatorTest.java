package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EveryOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EveryOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidInputs() {
        assertValidInput(EveryOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
        assertInvalidInput(EveryOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(EveryOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        assertInvalidArgument(EveryOperator.INSTANCE, 0, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
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
        assertGetAcceptableArgumentType(EveryOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentType(EveryOperator.INSTANCE, 1, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(EveryOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }
}
