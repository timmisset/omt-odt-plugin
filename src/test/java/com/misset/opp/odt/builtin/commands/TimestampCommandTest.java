package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimestampCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(TimeStampCommand.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("TIMESTAMP", TimeStampCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(TimeStampCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, TimeStampCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, TimeStampCommand.INSTANCE.maxNumberOfArguments());
    }
}
