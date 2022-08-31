package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LogOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LogOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance(), OppModelConstants.getXsdBooleanInstance());
        assertResolved(LogOperator.INSTANCE, OppModelConstants.getXsdDate(), OppModelConstants.getXsdDate());
        assertResolved(LogOperator.INSTANCE, OppModelConstants.getXsdInteger(), OppModelConstants.getXsdInteger());
    }

    @Test
    void testName() {
        Assertions.assertEquals("LOG", LogOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LogOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, LogOperator.INSTANCE.maxNumberOfArguments());
    }
}
