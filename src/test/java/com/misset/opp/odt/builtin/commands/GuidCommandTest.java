package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class GuidCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(GuidCommand.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }
}
