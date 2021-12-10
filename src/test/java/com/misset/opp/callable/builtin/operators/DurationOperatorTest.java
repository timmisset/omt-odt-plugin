package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class DurationOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DurationOperator.INSTANCE, oppModel.XSD_DURATION_INSTANCE);
    }

    @Test
    void testValidArguments() {
        testValidArgument(DurationOperator.INSTANCE, 0, oppModel.XSD_DECIMAL_INSTANCE);
        testValidArgument(DurationOperator.INSTANCE, 0, oppModel.XSD_INTEGER_INSTANCE);
        testValidArgument(DurationOperator.INSTANCE, 0, oppModel.XSD_NUMBER_INSTANCE);
        testInvalidArgument(DurationOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_NUMBER);
        testValidArgument(DurationOperator.INSTANCE, 1, oppModel.XSD_STRING_INSTANCE);
        testInvalidArgument(DurationOperator.INSTANCE, 1, oppModel.XSD_NUMBER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }
}
