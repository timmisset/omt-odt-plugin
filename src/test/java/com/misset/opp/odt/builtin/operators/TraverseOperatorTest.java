package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TraverseOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TraverseOperator.INSTANCE,
                Set.of(OntologyModelConstants.getOwlThingInstance()),
                Set.of(OntologyModelConstants.getOwlThingInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("TRAVERSE", TraverseOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, TraverseOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, TraverseOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(TraverseOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertValidArgument(TraverseOperator.INSTANCE, 1, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TraverseOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentType(TraverseOperator.INSTANCE, 1, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(TraverseOperator.INSTANCE, 2);
    }
}
