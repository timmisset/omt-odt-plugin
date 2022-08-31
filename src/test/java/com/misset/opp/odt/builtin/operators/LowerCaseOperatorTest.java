package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LowerCaseOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LowerCaseOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testValidArguments() {
        assertValidInput(LowerCaseOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
        assertInvalidInput(LowerCaseOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance(), TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LOWERCASE", LowerCaseOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LowerCaseOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, LowerCaseOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(LowerCaseOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
    }
}
