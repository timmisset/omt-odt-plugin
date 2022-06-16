package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LengthOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LengthOperator.INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidInput(LengthOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
        assertInvalidInput(LengthOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LENGTH", LengthOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LengthOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, LengthOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(LengthOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
    }
}
