package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WarningCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertCombinesInput(WarningCommand.INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("WARNING", WarningCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(-1, WarningCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, WarningCommand.INSTANCE.maxNumberOfArguments());
    }
}
