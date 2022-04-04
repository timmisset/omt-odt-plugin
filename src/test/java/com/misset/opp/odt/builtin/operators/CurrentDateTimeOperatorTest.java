package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CurrentDateTimeOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CurrentDateTimeOperator.INSTANCE, oppModel.XSD_DATETIME_INSTANCE);
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
    void testIsStatic() {
        Assertions.assertTrue(CurrentDateTimeOperator.INSTANCE.isStatic());
    }
}
