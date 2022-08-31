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
        assertResolved(DurationOperator.INSTANCE, OppModelConstants.getXsdDurationInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(DurationOperator.INSTANCE, 0, OppModelConstants.getXsdDecimalInstance());
        assertValidArgument(DurationOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance());
        assertValidArgument(DurationOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
        assertInvalidArgument(DurationOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance(), TTLValidationUtil.ERROR_MESSAGE_NUMBER);
        assertValidArgument(DurationOperator.INSTANCE, 1, OppModelConstants.getXsdStringInstance());
        assertInvalidArgument(DurationOperator.INSTANCE, 1, OppModelConstants.getXsdNumberInstance(), TTLValidationUtil.ERROR_MESSAGE_STRING);
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
        assertGetAcceptableArgumentType(DurationOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
        assertGetAcceptableArgumentType(DurationOperator.INSTANCE, 1, OppModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(DurationOperator.INSTANCE, 2);
    }

}
