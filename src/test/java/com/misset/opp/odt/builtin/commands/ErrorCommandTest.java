package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ErrorCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertCombinesInput(ErrorCommand.INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("ERROR", ErrorCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(-1, ErrorCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, ErrorCommand.INSTANCE.maxNumberOfArguments());
    }
}
