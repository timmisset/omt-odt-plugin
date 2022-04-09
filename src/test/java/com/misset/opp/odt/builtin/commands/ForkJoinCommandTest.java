package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ForkJoinCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertCombinesInput(ForkJoinCommand.INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FORKJOIN", ForkJoinCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(-1, ForkJoinCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, ForkJoinCommand.INSTANCE.maxNumberOfArguments());
    }
}
