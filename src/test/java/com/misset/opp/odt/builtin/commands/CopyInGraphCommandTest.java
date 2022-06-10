package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModel;
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
                OppModel.INSTANCE.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_INSTANCES,
                OppModel.INSTANCE.XSD_STRING);

        testArgument(CopyInGraphCommand.INSTANCE,
                1,
                OppModel.INSTANCE.MEDEWERKER_GRAPH,
                TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);

        testArgument(CopyInGraphCommand.INSTANCE,
                2,
                OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("COPY_IN_GRAPH", CopyInGraphCommand.INSTANCE.getName());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(CopyInGraphCommand.INSTANCE, 1, oppModel.NAMED_GRAPH);
        assertGetAcceptableArgumentType(CopyInGraphCommand.INSTANCE, 2, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(CopyInGraphCommand.INSTANCE, 3);
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, CopyInGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, CopyInGraphCommand.INSTANCE.maxNumberOfArguments());
    }
}
