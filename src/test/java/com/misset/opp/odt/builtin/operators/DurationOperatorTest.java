package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DurationOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DurationOperator.INSTANCE, OppModelConstants.XSD_DURATION_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(DurationOperator.INSTANCE, 0, OppModelConstants.XSD_DECIMAL_INSTANCE);
        assertValidArgument(DurationOperator.INSTANCE, 0, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertValidArgument(DurationOperator.INSTANCE, 0, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertInvalidArgument(DurationOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_NUMBER);
        assertValidArgument(DurationOperator.INSTANCE, 1, OppModelConstants.XSD_STRING_INSTANCE);
        assertInvalidArgument(DurationOperator.INSTANCE, 1, OppModelConstants.XSD_NUMBER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("DURATION", DurationOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, DurationOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, DurationOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(DurationOperator.INSTANCE, 0, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertGetAcceptableArgumentType(DurationOperator.INSTANCE, 1, OppModelConstants.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(DurationOperator.INSTANCE, 2);
    }

}
