package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SumOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SumOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance(), OntologyModelConstants.getXsdIntegerInstance());
        assertResolved(SumOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance(), OntologyModelConstants.getXsdDecimalInstance());
        assertResolved(SumOperator.INSTANCE, Set.of(OntologyModelConstants.getXsdIntegerInstance(), OntologyModelConstants.getXsdDecimalInstance()), Set.of(OntologyModelConstants.getXsdDecimalInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("SUM", SumOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, SumOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, SumOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(SumOperator.INSTANCE, OntologyModelConstants.getXsdNumberInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SumOperator.INSTANCE, OntologyModelConstants.getXsdNumberInstance());
    }
}
