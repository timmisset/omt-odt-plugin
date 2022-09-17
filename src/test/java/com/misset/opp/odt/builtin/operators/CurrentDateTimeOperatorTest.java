package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CurrentDateTimeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CurrentDateTimeOperator.INSTANCE, OntologyModelConstants.getXsdDatetimeInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("CURRENT_DATETIME", CurrentDateTimeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, CurrentDateTimeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, CurrentDateTimeOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testRequiresInput() {
        Assertions.assertFalse(CurrentDateTimeOperator.INSTANCE.requiresInput());
    }
}
