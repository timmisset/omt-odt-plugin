package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class TimestampCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(TimeStampCommand.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }
}
