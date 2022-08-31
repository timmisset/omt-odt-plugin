package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JoinOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(JoinOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("JOIN", JoinOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, JoinOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, JoinOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(JoinOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
        assertInvalidArgument(JoinOperator.INSTANCE, 0, OppModelConstants.getXsdBooleanInstance(), TTLValidationUtil.ERROR_MESSAGE_STRING);
        assertValidInput(JoinOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
        assertInvalidInput(JoinOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance(), TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(JoinOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(JoinOperator.INSTANCE, 1);
    }

}
