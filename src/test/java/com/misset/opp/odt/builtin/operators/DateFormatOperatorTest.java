package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateFormatOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DateFormatOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
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
        assertValidInput(DateFormatOperator.INSTANCE, OppModelConstants.getXsdDateInstance());
        assertValidInput(DateFormatOperator.INSTANCE, OppModelConstants.getXsdDatetimeInstance());
        testArgument(DateFormatOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance(), TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(DateFormatOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(DateFormatOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(DateFormatOperator.INSTANCE, OppModelConstants.getXsdDatetimeInstance());
    }
}
