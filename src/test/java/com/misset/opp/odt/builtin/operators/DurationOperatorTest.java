package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DurationOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DurationOperator.INSTANCE, OntologyModelConstants.getXsdDurationInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(DurationOperator.INSTANCE, 0, OntologyModelConstants.getXsdDecimalInstance());
        assertValidArgument(DurationOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
        assertValidArgument(DurationOperator.INSTANCE, 0, OntologyModelConstants.getXsdNumberInstance());
        assertInvalidArgument(DurationOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance(), OntologyValidationUtil.ERROR_MESSAGE_NUMBER);
        assertValidArgument(DurationOperator.INSTANCE, 1, OntologyModelConstants.getXsdStringInstance());
        assertInvalidArgument(DurationOperator.INSTANCE, 1, OntologyModelConstants.getXsdNumberInstance(), OntologyValidationUtil.ERROR_MESSAGE_STRING);
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
        assertGetAcceptableArgumentType(DurationOperator.INSTANCE, 0, OntologyModelConstants.getXsdNumberInstance());
        assertGetAcceptableArgumentType(DurationOperator.INSTANCE, 1, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(DurationOperator.INSTANCE, 2);
    }

}
