package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SplitOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SplitOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("SPLIT", SplitOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, SplitOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, SplitOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(SplitOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertValidArgument(SplitOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SplitOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(SplitOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(SplitOperator.INSTANCE, 1);
    }
}
