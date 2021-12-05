package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.callable.builtin.commands.JsonParseCommand;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class AndOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(AndOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(AndOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(AndOperator.INSTANCE, 1, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(AndOperator.INSTANCE, 2, oppModel.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }
}
