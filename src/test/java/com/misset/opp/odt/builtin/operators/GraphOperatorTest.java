package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class GraphOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GraphOperator.INSTANCE, oppModel.NAMED_GRAPH);
    }

    @Test
    void testInputArgumentsRequiresInstances() {
        testValidInput(GraphOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        testInvalidInput(GraphOperator.INSTANCE, oppModel.XSD_STRING, TTLValidationUtil.ERROR_MESSAGE_INSTANCES);
    }
}
