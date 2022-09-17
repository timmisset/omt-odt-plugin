package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class GreaterThanOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GreaterThanOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OntologyModelConstants.getXsdStringInstance());
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OntologyModelConstants.getXsdNumberInstance());
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OntologyModelConstants.getXsdIntegerInstance());
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OntologyModelConstants.getXsdDecimalInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("GREATER_THAN", GreaterThanOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, GreaterThanOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, GreaterThanOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(GreaterThanOperator.INSTANCE, 0, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
        assertGetAcceptableArgumentType(GreaterThanOperator.INSTANCE, 1, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(GreaterThanOperator.INSTANCE, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
    }
}
