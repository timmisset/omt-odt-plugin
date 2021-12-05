package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;

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
