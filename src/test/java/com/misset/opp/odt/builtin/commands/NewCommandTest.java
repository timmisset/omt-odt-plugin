package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NewCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(NewCommand.INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("NEW", NewCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(NewCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, NewCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, NewCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        testArgument(NewCommand.INSTANCE, 0, OntologyModelConstants.getXsdString(), OntologyValidationUtil.ERROR_MESSAGE_CLASSNAME);
        testArgument(NewCommand.INSTANCE, 1, OntologyModelConstants.getMedewerkerGraph(), OntologyValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(NewCommand.INSTANCE, 1, OntologyModelConstants.getNamedGraph());
        assertGetAcceptableArgumentTypeIsNull(NewCommand.INSTANCE, 0);
    }
}
