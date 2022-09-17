package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AssertCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(AssertCommand.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
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
