package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.Individual;
import org.junit.jupiter.api.Test;

class NewGraphCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(NewGraphCommand.INSTANCE, oppModel.NAMED_GRAPH);
    }

    @Test
    void testArgumentTypes() {
        final Individual shapeIndividual = OppModel.INSTANCE.GRAPH_SHAPE.createIndividual(
                "http://ontology#someGraphShape");
        testArgument(NewGraphCommand.INSTANCE, 0, shapeIndividual, TTLValidationUtil.ERROR_MESSAGE_GRAPH_SHAPE);
    }
}
