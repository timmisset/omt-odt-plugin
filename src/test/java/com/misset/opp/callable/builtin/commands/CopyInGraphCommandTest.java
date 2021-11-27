package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.validation.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class CopyInGraphCommandTest extends BuiltInTest {

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
}
