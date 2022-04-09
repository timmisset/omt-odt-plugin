package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SomeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SomeOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidInputs() {
        assertValidInput(SomeOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
        assertInvalidInput(SomeOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(SomeOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        assertInvalidArgument(SomeOperator.INSTANCE, 0, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("SOME", SomeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, SomeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, SomeOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(SomeOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(SomeOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SomeOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }
}
