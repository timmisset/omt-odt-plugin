package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EveryOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EveryOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidInputs() {
        assertValidInput(EveryOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertInvalidInput(EveryOperator.INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(EveryOperator.INSTANCE, 0, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertInvalidArgument(EveryOperator.INSTANCE, 0, OppModelConstants.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("EVERY", EveryOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, EveryOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, EveryOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(EveryOperator.INSTANCE, 0, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentType(EveryOperator.INSTANCE, 1, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(EveryOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }
}
