package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClearGraphCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(ClearGraphCommand.INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(ClearGraphCommand.INSTANCE,
                0,
                OppModelConstants.MEDEWERKER_GRAPH,
                TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
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
        assertGetAcceptableArgumentType(ClearGraphCommand.INSTANCE, 0, OppModelConstants.NAMED_GRAPH);
        assertGetAcceptableArgumentTypeIsNull(ClearGraphCommand.INSTANCE, 1);
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, ClearGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, ClearGraphCommand.INSTANCE.maxNumberOfArguments());
    }
}
