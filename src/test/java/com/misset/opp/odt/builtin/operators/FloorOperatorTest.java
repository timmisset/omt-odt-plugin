package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FloorOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FloorOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
        assertResolved(FloorOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testValidInputs() {
        assertValidInput(FloorOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE);
        assertInvalidInput(FloorOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_DECIMAL);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FLOOR", FloorOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, FloorOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, FloorOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(FloorOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE);
    }
}
