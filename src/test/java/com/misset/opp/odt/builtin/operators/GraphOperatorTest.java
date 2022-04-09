package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GraphOperator.INSTANCE, oppModel.NAMED_GRAPH);
    }

    @Test
    void testInputArgumentsRequiresInstances() {
        assertValidInput(GraphOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        assertInvalidInput(GraphOperator.INSTANCE, oppModel.XSD_STRING, TTLValidationUtil.ERROR_MESSAGE_INSTANCES);
    }

    @Test
    void testName() {
        Assertions.assertEquals("GRAPH", GraphOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, GraphOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, GraphOperator.INSTANCE.maxNumberOfArguments());
    }
}
