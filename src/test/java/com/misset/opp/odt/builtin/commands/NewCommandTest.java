package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NewCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(NewCommand.INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("NEW", NewCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(NewCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, NewCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, NewCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        testArgument(NewCommand.INSTANCE, 0, OppModelConstants.XSD_STRING, TTLValidationUtil.ERROR_MESSAGE_CLASSNAME);
        testArgument(NewCommand.INSTANCE, 1, OppModelConstants.MEDEWERKER_GRAPH, TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(NewCommand.INSTANCE, 1, OppModelConstants.NAMED_GRAPH);
        assertGetAcceptableArgumentTypeIsNull(NewCommand.INSTANCE, 0);
    }
}
