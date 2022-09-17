package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentityOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IdentityOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyModelConstants.getXsdBooleanInstance());
        assertResolved(IdentityOperator.INSTANCE, OntologyModelConstants.getXsdDate(), OntologyModelConstants.getXsdDate());
        assertResolved(IdentityOperator.INSTANCE, OntologyModelConstants.getXsdInteger(), OntologyModelConstants.getXsdInteger());
    }

    @Test
    void testName() {
        Assertions.assertEquals("IDENTITY", IdentityOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, IdentityOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, IdentityOperator.INSTANCE.maxNumberOfArguments());
    }
}
