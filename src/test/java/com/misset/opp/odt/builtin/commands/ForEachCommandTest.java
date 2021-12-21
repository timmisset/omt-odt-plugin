package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class ForEachCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsVoid(DestroyCommand.INSTANCE);
    }
}
