package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UpperCaseOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(UpperCaseOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidInput(UpperCaseOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        assertInvalidInput(UpperCaseOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("UPPERCASE", UpperCaseOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, UpperCaseOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, UpperCaseOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(UpperCaseOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }
}
