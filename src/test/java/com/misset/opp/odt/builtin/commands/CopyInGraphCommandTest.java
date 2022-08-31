package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CopyInGraphCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(CopyInGraphCommand.INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(CopyInGraphCommand.INSTANCE,
                0,
                OppModelConstants.getXsdStringInstance(),
                TTLValidationUtil.ERROR_MESSAGE_INSTANCES,
                OppModelConstants.getXsdString());

        testArgument(CopyInGraphCommand.INSTANCE,
                1,
                OppModelConstants.getMedewerkerGraph(),
                TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);

        testArgument(CopyInGraphCommand.INSTANCE,
                2,
                OppModelConstants.getXsdBooleanInstance(),
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("COPY_IN_GRAPH", CopyInGraphCommand.INSTANCE.getName());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(CopyInGraphCommand.INSTANCE, 1, OppModelConstants.getNamedGraph());
        assertGetAcceptableArgumentType(CopyInGraphCommand.INSTANCE, 2, OppModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(CopyInGraphCommand.INSTANCE, 3);
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, CopyInGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, CopyInGraphCommand.INSTANCE.maxNumberOfArguments());
    }
}
