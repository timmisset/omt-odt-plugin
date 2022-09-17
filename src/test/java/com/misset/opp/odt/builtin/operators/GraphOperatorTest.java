package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GraphOperator.INSTANCE, OntologyModelConstants.getNamedGraph());
    }

    @Test
    void testInputArgumentsRequiresInstances() {
        assertValidInput(GraphOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertInvalidInput(GraphOperator.INSTANCE, OntologyModelConstants.getXsdString(), OntologyValidationUtil.ERROR_MESSAGE_INSTANCES);
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
