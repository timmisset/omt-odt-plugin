package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class MoveToGraphCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(MoveToGraphCommand.INSTANCE);
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
}
