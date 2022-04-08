package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FirstOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FirstOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
        assertResolved(FirstOperator.INSTANCE, oppModel.XSD_DATE, oppModel.XSD_DATE);
        assertResolved(FirstOperator.INSTANCE, oppModel.XSD_INTEGER, oppModel.XSD_INTEGER);
    }

    @Test
    void testValidArguments() {
        testValidArgument(FirstOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        testInvalidArgument(FirstOperator.INSTANCE, 0, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FIRST", FirstOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, FirstOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, FirstOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(FirstOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(FirstOperator.INSTANCE, 1);
    }
}
