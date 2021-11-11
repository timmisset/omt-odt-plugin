package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class GuidCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(GuidCommand.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }
}
