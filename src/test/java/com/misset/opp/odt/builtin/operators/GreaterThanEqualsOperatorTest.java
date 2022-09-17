package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class GreaterThanEqualsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GreaterThanEqualsOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, OntologyModelConstants.getXsdStringInstance());
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, OntologyModelConstants.getXsdNumberInstance());
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, OntologyModelConstants.getXsdIntegerInstance());
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, OntologyModelConstants.getXsdDecimalInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("GREATER_THAN_EQUALS", GreaterThanEqualsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, GreaterThanEqualsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, GreaterThanEqualsOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(GreaterThanEqualsOperator.INSTANCE, 0, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
        assertGetAcceptableArgumentType(GreaterThanEqualsOperator.INSTANCE, 1, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(GreaterThanEqualsOperator.INSTANCE, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
    }
}
