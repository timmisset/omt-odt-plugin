package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SplitOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SplitOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
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
        assertValidInput(SplitOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
        assertValidArgument(SplitOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SplitOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(SplitOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(SplitOperator.INSTANCE, 1);
    }
}
