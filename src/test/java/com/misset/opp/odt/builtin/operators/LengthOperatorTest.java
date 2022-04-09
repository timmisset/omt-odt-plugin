package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LengthOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LengthOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidInput(LengthOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        assertInvalidInput(LengthOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LENGTH", LengthOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LengthOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, LengthOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(LengthOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }
}
