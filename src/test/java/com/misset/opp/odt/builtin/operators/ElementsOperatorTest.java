package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ElementsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ElementsOperator.INSTANCE, OntologyModelConstants.getOwlThingInstance());
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
