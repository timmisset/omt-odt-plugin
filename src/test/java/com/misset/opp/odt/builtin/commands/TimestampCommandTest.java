package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimestampCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(TimeStampCommand.INSTANCE, OntologyModelConstants.getXsdIntegerInstance());
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
