package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContainsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ContainsOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testArgumentTypes() {
        assertValidInput(ContainsOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
        assertInvalidInput(ContainsOperator.INSTANCE, OppModelConstants.getXsdDecimalInstance(), TTLValidationUtil.ERROR_MESSAGE_STRING);
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
        assertGetAcceptableArgumentType(ContainsOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentType(ContainsOperator.INSTANCE, 1, OppModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(ContainsOperator.INSTANCE, 2);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(ContainsOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetFlags() {
        Assertions.assertEquals(AbstractBuiltInOperator.IGNORE_CASE_FLAG, ContainsOperator.INSTANCE.getFlags());
    }
}
