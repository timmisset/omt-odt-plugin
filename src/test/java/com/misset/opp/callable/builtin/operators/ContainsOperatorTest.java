package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class ContainsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ContainsOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }


    @Test
    void testArgumentTypes() {
        testValidInput(ContainsOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
        testInvalidInput(ContainsOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }
}
