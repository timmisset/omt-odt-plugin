package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LogOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LogOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyModelConstants.getXsdBooleanInstance());
        assertResolved(LogOperator.INSTANCE, OntologyModelConstants.getXsdDate(), OntologyModelConstants.getXsdDate());
        assertResolved(LogOperator.INSTANCE, OntologyModelConstants.getXsdInteger(), OntologyModelConstants.getXsdInteger());
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
