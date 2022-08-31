package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GraphOperator.INSTANCE, OppModelConstants.getNamedGraph());
    }

    @Test
    void testInputArgumentsRequiresInstances() {
        assertValidInput(GraphOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
        assertInvalidInput(GraphOperator.INSTANCE, OppModelConstants.getXsdString(), TTLValidationUtil.ERROR_MESSAGE_INSTANCES);
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
