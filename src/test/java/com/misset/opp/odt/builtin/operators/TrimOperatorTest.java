package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TrimOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TrimOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("TRIM", TrimOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, TrimOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, TrimOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(TrimOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(TrimOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }
}
