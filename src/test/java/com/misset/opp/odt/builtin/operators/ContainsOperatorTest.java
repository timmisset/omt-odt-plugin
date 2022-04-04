package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContainsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ContainsOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        assertValidInput(ContainsOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        testInvalidInput(ContainsOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("CONTAINS", ContainsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, ContainsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, ContainsOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(ContainsOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentType(ContainsOperator.INSTANCE, 1, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(ContainsOperator.INSTANCE, 2);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(ContainsOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testGetFlags() {
        Assertions.assertEquals(ContainsOperator.IGNORE_CASE_FLAG, ContainsOperator.INSTANCE.getFlags());
    }
}
