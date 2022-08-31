package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ElementsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ElementsOperator.INSTANCE, OppModelConstants.getOwlThingInstance());
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
