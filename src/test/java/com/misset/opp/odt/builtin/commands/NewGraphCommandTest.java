package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.Individual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NewGraphCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(NewGraphCommand.INSTANCE, OppModelConstants.NAMED_GRAPH);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("NEW_GRAPH", NewGraphCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(NewGraphCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, NewGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, NewGraphCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        final Individual shapeIndividual =
                oppModel.createIndividual(OppModelConstants.GRAPH_SHAPE, "http://ontology#someGraphShape");
        testArgument(NewGraphCommand.INSTANCE, 0, shapeIndividual, TTLValidationUtil.ERROR_MESSAGE_GRAPH_SHAPE);
    }

}
