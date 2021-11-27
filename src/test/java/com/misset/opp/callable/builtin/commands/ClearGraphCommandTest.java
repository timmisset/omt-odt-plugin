package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.validation.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class ClearGraphCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(ClearGraphCommand.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(ClearGraphCommand.INSTANCE,
                0,
                OppModel.INSTANCE.MEDEWERKER_GRAPH,
                TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }
}
