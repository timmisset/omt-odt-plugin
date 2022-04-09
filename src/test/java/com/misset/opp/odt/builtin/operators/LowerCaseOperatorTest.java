package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LowerCaseOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LowerCaseOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidInput(LowerCaseOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        assertInvalidInput(LowerCaseOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LOWERCASE", LowerCaseOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LowerCaseOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, LowerCaseOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(LowerCaseOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }
}
