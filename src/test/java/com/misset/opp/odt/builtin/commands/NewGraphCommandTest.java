package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.apache.jena.ontology.Individual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NewGraphCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(NewGraphCommand.INSTANCE, OntologyModelConstants.getNamedGraph());
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
                ontologyModel.createIndividual(OntologyModelConstants.getGraphShape(), "http://ontology#someGraphShape");
        testArgument(NewGraphCommand.INSTANCE, 0, shapeIndividual, OntologyValidationUtil.ERROR_MESSAGE_GRAPH_SHAPE);
    }

}
