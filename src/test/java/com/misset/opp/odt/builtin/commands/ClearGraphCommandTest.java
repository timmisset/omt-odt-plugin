package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
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
