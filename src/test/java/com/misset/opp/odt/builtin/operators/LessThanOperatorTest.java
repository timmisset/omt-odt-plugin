package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class LessThanOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LessThanOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(LessThanOperator.INSTANCE, 1, OntologyModelConstants.getXsdStringInstance());
        assertValidArgument(LessThanOperator.INSTANCE, 1, OntologyModelConstants.getXsdNumberInstance());
        assertValidArgument(LessThanOperator.INSTANCE, 1, OntologyModelConstants.getXsdIntegerInstance());
        assertValidArgument(LessThanOperator.INSTANCE, 1, OntologyModelConstants.getXsdDecimalInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("LESS_THAN", LessThanOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, LessThanOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, LessThanOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(LessThanOperator.INSTANCE, 0, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
        assertGetAcceptableArgumentType(LessThanOperator.INSTANCE, 1, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(LessThanOperator.INSTANCE, Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance()));
    }
}
