package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ElementsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ElementsOperator.INSTANCE, oppModel.OWL_THING_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("ELEMENTS", ElementsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, ElementsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, ElementsOperator.INSTANCE.maxNumberOfArguments());
    }
}
