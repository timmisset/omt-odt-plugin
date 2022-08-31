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
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()));
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
        assertValidInput(MaxOperator.INSTANCE, OppModelConstants.getXsdNumberInstance());
        assertValidInput(MaxOperator.INSTANCE, OppModelConstants.getXsdDatetimeInstance());
        assertInvalidInput(MaxOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), "Acceptable types:");

        assertValidArgument(MaxOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
        assertValidArgument(MaxOperator.INSTANCE, 0, OppModelConstants.getXsdDatetimeInstance());
        assertInvalidArgument(MaxOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance(), "Acceptable types:");
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(MaxOperator.INSTANCE, Set.of(OppModelConstants.getXsdNumberInstance(), OppModelConstants.getXsdDatetimeInstance()));
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MaxOperator.INSTANCE, 0, Set.of(OppModelConstants.getXsdNumberInstance(), OppModelConstants.getXsdDatetimeInstance()));
        assertGetAcceptableArgumentTypeIsNull(MaxOperator.INSTANCE, 1);
    }
}
