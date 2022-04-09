package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JoinOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(JoinOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("JOIN", JoinOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, JoinOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, JoinOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(JoinOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertInvalidArgument(JoinOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
        assertValidInput(JoinOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        assertInvalidInput(JoinOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(JoinOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(JoinOperator.INSTANCE, 1);
    }

}
