package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TypeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TypeOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdDateInstance()),
                Set.of(OppModelConstants.getXsdDate()));

        assertResolved(TypeOperator.INSTANCE,
                Set.of(OppModelConstants.getMedewerkerGraph()),
                Set.of(OppModelConstants.getIri()));
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
