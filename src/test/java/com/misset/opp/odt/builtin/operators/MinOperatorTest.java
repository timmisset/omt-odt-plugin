package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MinOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MinOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("MIN", MinOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, MinOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, MinOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(MinOperator.INSTANCE, OppModelConstants.getXsdNumberInstance());
        assertValidInput(MinOperator.INSTANCE, OppModelConstants.getXsdDatetimeInstance());
        assertInvalidInput(MinOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), "Acceptable types:");

        assertValidArgument(MinOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
        assertValidArgument(MinOperator.INSTANCE, 0, OppModelConstants.getXsdDatetimeInstance());
        assertInvalidArgument(MinOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance(), "Acceptable types:");
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(MinOperator.INSTANCE, Set.of(OppModelConstants.getXsdNumberInstance(), OppModelConstants.getXsdDatetimeInstance()));
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MinOperator.INSTANCE, 0, Set.of(OppModelConstants.getXsdNumberInstance(), OppModelConstants.getXsdDatetimeInstance()));
        assertGetAcceptableArgumentTypeIsNull(MinOperator.INSTANCE, 1);
    }
}
