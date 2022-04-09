package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IndexOfOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IndexOfOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("INDEX_OF", IndexOfOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, IndexOfOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, IndexOfOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(IndexOfOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(IndexOfOperator.INSTANCE, 1);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(IndexOfOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertInvalidArgument(IndexOfOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
        assertValidInput(IndexOfOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        assertInvalidInput(IndexOfOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }
}
