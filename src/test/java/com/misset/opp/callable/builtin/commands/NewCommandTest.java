package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class NewCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(NewCommand.INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(NewCommand.INSTANCE, 0, oppModel.XSD_STRING, TTLValidationUtil.ERROR_MESSAGE_CLASSNAME);
        testArgument(NewCommand.INSTANCE, 1, oppModel.MEDEWERKER_GRAPH, TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }
}
