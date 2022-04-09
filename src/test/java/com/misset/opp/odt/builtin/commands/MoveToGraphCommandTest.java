package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
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
                oppModel.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_INSTANCES,
                oppModel.XSD_STRING);
        testArgument(MoveToGraphCommand.INSTANCE,
                1,
                oppModel.MEDEWERKER_GRAPH,
                TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MoveToGraphCommand.INSTANCE, 1, oppModel.NAMED_GRAPH);
        assertGetAcceptableArgumentTypeIsNull(MoveToGraphCommand.INSTANCE, 0);
    }
}
