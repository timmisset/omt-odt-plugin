package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UpperCaseOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(UpperCaseOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testValidArguments() {
        assertValidInput(UpperCaseOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
        assertInvalidInput(UpperCaseOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance(), TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("UPPERCASE", UpperCaseOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, UpperCaseOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, UpperCaseOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(UpperCaseOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
    }
}
