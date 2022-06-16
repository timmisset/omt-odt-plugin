package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LastOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LastOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertResolved(LastOperator.INSTANCE, OppModelConstants.XSD_DATE, OppModelConstants.XSD_DATE);
        assertResolved(LastOperator.INSTANCE, OppModelConstants.XSD_INTEGER, OppModelConstants.XSD_INTEGER);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(LastOperator.INSTANCE, 0, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertInvalidArgument(LastOperator.INSTANCE, 0, OppModelConstants.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LAST", LastOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LastOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, LastOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(LastOperator.INSTANCE, 0, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(LastOperator.INSTANCE, 1);
    }
}
