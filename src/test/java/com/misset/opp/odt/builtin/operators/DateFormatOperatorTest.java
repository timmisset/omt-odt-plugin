package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateFormatOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DateFormatOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FORMAT_DATE", DateFormatOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, DateFormatOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, DateFormatOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        assertValidInput(DateFormatOperator.INSTANCE, oppModel.XSD_DATE_INSTANCE);
        assertValidInput(DateFormatOperator.INSTANCE, oppModel.XSD_DATETIME_INSTANCE);
        testArgument(DateFormatOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(DateFormatOperator.INSTANCE, 0, OppModel.INSTANCE.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(DateFormatOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(DateFormatOperator.INSTANCE, oppModel.XSD_DATETIME_INSTANCE);
    }
}
