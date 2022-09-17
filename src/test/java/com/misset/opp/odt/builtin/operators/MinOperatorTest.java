package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MinOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MinOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdBooleanInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance()));
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
        assertValidInput(MinOperator.INSTANCE, OntologyModelConstants.getXsdNumberInstance());
        assertValidInput(MinOperator.INSTANCE, OntologyModelConstants.getXsdDatetimeInstance());
        assertInvalidInput(MinOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), "Acceptable types:");

        assertValidArgument(MinOperator.INSTANCE, 0, OntologyModelConstants.getXsdNumberInstance());
        assertValidArgument(MinOperator.INSTANCE, 0, OntologyModelConstants.getXsdDatetimeInstance());
        assertInvalidArgument(MinOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance(), "Acceptable types:");
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(MinOperator.INSTANCE, Set.of(OntologyModelConstants.getXsdNumberInstance(), OntologyModelConstants.getXsdDatetimeInstance()));
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MinOperator.INSTANCE, 0, Set.of(OntologyModelConstants.getXsdNumberInstance(), OntologyModelConstants.getXsdDatetimeInstance()));
        assertGetAcceptableArgumentTypeIsNull(MinOperator.INSTANCE, 1);
    }
}
