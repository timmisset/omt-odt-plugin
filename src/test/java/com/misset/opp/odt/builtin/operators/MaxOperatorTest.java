package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MaxOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MaxOperator.INSTANCE,
                Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE),
                Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("MAX", MaxOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, MaxOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, MaxOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(MaxOperator.INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidInput(MaxOperator.INSTANCE, OppModelConstants.XSD_DATETIME_INSTANCE);
        assertInvalidInput(MaxOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, "Acceptable types:");

        assertValidArgument(MaxOperator.INSTANCE, 0, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidArgument(MaxOperator.INSTANCE, 0, OppModelConstants.XSD_DATETIME_INSTANCE);
        assertInvalidArgument(MaxOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE, "Acceptable types:");
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(MaxOperator.INSTANCE, Set.of(OppModelConstants.XSD_NUMBER_INSTANCE, OppModelConstants.XSD_DATETIME_INSTANCE));
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MaxOperator.INSTANCE, 0, Set.of(OppModelConstants.XSD_NUMBER_INSTANCE, OppModelConstants.XSD_DATETIME_INSTANCE));
        assertGetAcceptableArgumentTypeIsNull(MaxOperator.INSTANCE, 1);
    }
}
