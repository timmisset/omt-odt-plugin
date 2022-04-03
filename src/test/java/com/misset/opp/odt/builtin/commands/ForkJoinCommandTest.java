package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ForkJoinCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertCombinesInput(ForkJoinCommand.INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FORK_JOIN", ForkJoinCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(-1, ForkJoinCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, ForkJoinCommand.INSTANCE.maxNumberOfArguments());
    }
}
