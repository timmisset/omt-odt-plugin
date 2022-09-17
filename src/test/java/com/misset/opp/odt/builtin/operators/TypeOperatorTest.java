package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TypeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TypeOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdDateInstance()),
                Set.of(OntologyModelConstants.getXsdDate()));

        assertResolved(TypeOperator.INSTANCE,
                Set.of(OntologyModelConstants.getMedewerkerGraph()),
                Set.of(OntologyModelConstants.getIri()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("TYPE", TypeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, TypeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, TypeOperator.INSTANCE.maxNumberOfArguments());
    }
}
