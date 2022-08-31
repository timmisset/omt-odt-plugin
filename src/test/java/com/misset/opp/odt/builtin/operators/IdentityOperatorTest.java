package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentityOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IdentityOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance(), OppModelConstants.getXsdBooleanInstance());
        assertResolved(IdentityOperator.INSTANCE, OppModelConstants.getXsdDate(), OppModelConstants.getXsdDate());
        assertResolved(IdentityOperator.INSTANCE, OppModelConstants.getXsdInteger(), OppModelConstants.getXsdInteger());
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
