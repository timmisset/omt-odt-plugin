package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class FloorOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FloorOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
        assertResolved(FloorOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testValidInputs() {
        assertValidInput(FloorOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE);
        testInvalidInput(FloorOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_DECIMAL);
    }

}
