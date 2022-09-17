package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PickOperatorTest extends BaseBuiltinTest {

    @Test
    void testName() {
        Assertions.assertEquals("PICK", PickOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, PickOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, PickOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(PickOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(PickOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
        assertGetAcceptableArgumentTypeIsNull(PickOperator.INSTANCE, 1);
    }

    @Override
    protected void testResolve() {
        assertResolved(PickOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdStringInstance());
    }
}
