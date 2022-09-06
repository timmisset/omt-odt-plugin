package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LogCommandTest extends BaseBuiltinTest {

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
    void testNumberOfArguments() {
        Assertions.assertEquals(-1, LogCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, LogCommand.INSTANCE.maxNumberOfArguments());
    }
}
