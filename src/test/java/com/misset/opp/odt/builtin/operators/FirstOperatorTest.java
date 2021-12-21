package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class FirstOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FirstOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
        assertResolved(FirstOperator.INSTANCE, oppModel.XSD_DATE, oppModel.XSD_DATE);
        assertResolved(FirstOperator.INSTANCE, oppModel.XSD_INTEGER, oppModel.XSD_INTEGER);
    }

    @Test
    void testValidArguments() {
        testValidArgument(EveryOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        testInvalidArgument(EveryOperator.INSTANCE, 0, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }
}
