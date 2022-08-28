package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoadOntologyCommandTest extends BaseBuiltinTest {

    @Test
    void testGetDescription() {
        Assertions.assertEquals(LoadOntologyCommand.DESCRIPTION, LoadOntologyCommand.INSTANCE.getDescription(null));
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("LOAD_ONTOLOGY", LoadOntologyCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertTrue(LoadOntologyCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, LoadOntologyCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, LoadOntologyCommand.INSTANCE.maxNumberOfArguments());
    }

    @Override
    protected void testResolve() {
        assertReturnsVoid(LoadOntologyCommand.INSTANCE);
    }
}
