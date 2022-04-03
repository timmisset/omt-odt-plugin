package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GuidCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(GuidCommand.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("GUID", GuidCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, GuidCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, GuidCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testIsVoidReturnsFalse() {
        Assertions.assertFalse(GuidCommand.INSTANCE.isVoid());
    }
}
