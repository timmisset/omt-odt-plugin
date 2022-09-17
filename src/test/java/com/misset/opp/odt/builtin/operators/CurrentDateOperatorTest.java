package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CurrentDateOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CurrentDateOperator.INSTANCE, OntologyModelConstants.getXsdDateInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("CURRENT_DATE", CurrentDateOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, CurrentDateOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, CurrentDateOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testRequiresInput() {
        Assertions.assertFalse(CurrentDateOperator.INSTANCE.requiresInput());
    }
}
