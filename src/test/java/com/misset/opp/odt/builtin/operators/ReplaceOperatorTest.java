package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
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
        assertValidInput(ReplaceOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertValidArgument(ReplaceOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertValidArgument(ReplaceOperator.INSTANCE, 1, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(ReplaceOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(ReplaceOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
    }

    @Override
    protected void testResolve() {
        assertResolved(PickOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdStringInstance());
    }
}
