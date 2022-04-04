package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LogCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertCombinesInput(LogCommand.INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("LOG", LogCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertTrue(LogCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(-1, LogCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, LogCommand.INSTANCE.maxNumberOfArguments());
    }
}
