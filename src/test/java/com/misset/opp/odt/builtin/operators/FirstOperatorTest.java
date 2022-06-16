package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FirstOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FirstOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertResolved(FirstOperator.INSTANCE, OppModelConstants.XSD_DATE, OppModelConstants.XSD_DATE);
        assertResolved(FirstOperator.INSTANCE, OppModelConstants.XSD_INTEGER, OppModelConstants.XSD_INTEGER);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FirstOperator.INSTANCE, 0, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertInvalidArgument(FirstOperator.INSTANCE, 0, OppModelConstants.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
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
        assertGetAcceptableArgumentType(FirstOperator.INSTANCE, 0, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(FirstOperator.INSTANCE, 1);
    }
}
