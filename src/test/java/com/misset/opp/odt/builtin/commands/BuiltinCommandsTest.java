package com.misset.opp.odt.builtin.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class BuiltinCommandsTest {

    @Test
    void testHasBuiltinCommands() {
        assertFalse(BuiltinCommands.getCommands().isEmpty());
    }
}
