package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MaxOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MaxOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdBooleanInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance()));
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
        assertValidInput(MaxOperator.INSTANCE, OntologyModelConstants.getXsdNumberInstance());
        assertValidInput(MaxOperator.INSTANCE, OntologyModelConstants.getXsdDatetimeInstance());
        assertInvalidInput(MaxOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), "Acceptable types:");

        assertValidArgument(MaxOperator.INSTANCE, 0, OntologyModelConstants.getXsdNumberInstance());
        assertValidArgument(MaxOperator.INSTANCE, 0, OntologyModelConstants.getXsdDatetimeInstance());
        assertInvalidArgument(MaxOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance(), "Acceptable types:");
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(MaxOperator.INSTANCE, Set.of(OntologyModelConstants.getXsdNumberInstance(), OntologyModelConstants.getXsdDatetimeInstance()));
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MaxOperator.INSTANCE, 0, Set.of(OntologyModelConstants.getXsdNumberInstance(), OntologyModelConstants.getXsdDatetimeInstance()));
        assertGetAcceptableArgumentTypeIsNull(MaxOperator.INSTANCE, 1);
    }
}
