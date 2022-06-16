package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NewTransientGraphCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(NewTransientGraphCommand.INSTANCE, OppModelConstants.NAMED_GRAPH);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("NEW_TRANSIENT_GRAPH", NewTransientGraphCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(NewTransientGraphCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, NewTransientGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, NewTransientGraphCommand.INSTANCE.maxNumberOfArguments());
    }
}
