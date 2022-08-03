package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AssertCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(AssertCommand.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("ASSERT", AssertCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, AssertCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, AssertCommand.INSTANCE.maxNumberOfArguments());
    }
}
