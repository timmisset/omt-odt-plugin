package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NotOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(NotOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("NOT", NotOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, NotOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, NotOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(NotOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
        assertValidArgument(NotOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(NotOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(NotOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(NotOperator.INSTANCE, 1);
    }
}
