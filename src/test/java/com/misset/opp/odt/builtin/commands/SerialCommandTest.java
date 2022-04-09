package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SerialCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertCombinesInput(SerialCommand.INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("SERIAL", SerialCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertTrue(SerialCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(-1, SerialCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, SerialCommand.INSTANCE.maxNumberOfArguments());
    }
}
