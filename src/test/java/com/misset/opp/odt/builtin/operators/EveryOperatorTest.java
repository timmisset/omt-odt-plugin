package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class EveryOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EveryOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidInputs() {
        assertValidInput(EveryOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
        testInvalidInput(EveryOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidArguments() {
        testValidArgument(EveryOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        testInvalidArgument(EveryOperator.INSTANCE, 0, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

}
