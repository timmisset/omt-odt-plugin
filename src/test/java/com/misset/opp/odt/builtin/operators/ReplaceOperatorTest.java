package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReplaceOperatorTest extends BaseBuiltinTest {
    @Test
    void testName() {
        Assertions.assertEquals("REPLACE", ReplaceOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, ReplaceOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, ReplaceOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(ReplaceOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
        assertValidArgument(ReplaceOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
        assertValidArgument(ReplaceOperator.INSTANCE, 1, OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(ReplaceOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(ReplaceOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Override
    protected void testResolve() {
        assertResolved(PickOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
    }
}
