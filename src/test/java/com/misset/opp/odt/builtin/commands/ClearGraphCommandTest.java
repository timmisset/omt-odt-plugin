package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClearGraphCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(ClearGraphCommand.INSTANCE, OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testArgumentTypes() {
        testArgument(ClearGraphCommand.INSTANCE,
                0,
                OntologyModelConstants.getMedewerkerGraph(),
                OntologyValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testName() {
        Assertions.assertEquals("CLEAR_GRAPH", ClearGraphCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(ClearGraphCommand.INSTANCE.isVoid());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(ClearGraphCommand.INSTANCE, 0, OntologyModelConstants.getNamedGraph());
        assertGetAcceptableArgumentTypeIsNull(ClearGraphCommand.INSTANCE, 1);
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, ClearGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, ClearGraphCommand.INSTANCE.maxNumberOfArguments());
    }
}
