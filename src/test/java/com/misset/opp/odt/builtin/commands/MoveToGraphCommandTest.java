package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveToGraphCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(MoveToGraphCommand.INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("MOVE_TO_GRAPH", MoveToGraphCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(MoveToGraphCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, MoveToGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, MoveToGraphCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        testArgument(MoveToGraphCommand.INSTANCE,
                0,
                OntologyModelConstants.getXsdStringInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_INSTANCES,
                OntologyModelConstants.getXsdString());
        testArgument(MoveToGraphCommand.INSTANCE,
                1,
                OntologyModelConstants.getMedewerkerGraph(),
                OntologyValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MoveToGraphCommand.INSTANCE, 1, OntologyModelConstants.getNamedGraph());
        assertGetAcceptableArgumentTypeIsNull(MoveToGraphCommand.INSTANCE, 0);
    }
}
