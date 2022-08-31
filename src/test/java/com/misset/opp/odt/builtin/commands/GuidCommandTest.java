package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GuidCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(GuidCommand.INSTANCE, OppModelConstants.getXsdStringInstance());
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
