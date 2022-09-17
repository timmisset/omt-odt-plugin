package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BlankNodeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(BlankNodeOperator.INSTANCE, OntologyModelConstants.getBlankNode());
    }

    @Test
    void testName() {
        Assertions.assertEquals("BLANK_NODE", BlankNodeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, BlankNodeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, BlankNodeOperator.INSTANCE.maxNumberOfArguments());
    }

}
