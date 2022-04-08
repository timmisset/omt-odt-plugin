package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmptyOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EmptyOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("EMPTY", EmptyOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, EmptyOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, EmptyOperator.INSTANCE.maxNumberOfArguments());
    }
}
