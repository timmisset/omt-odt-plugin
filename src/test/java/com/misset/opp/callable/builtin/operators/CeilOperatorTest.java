package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class CeilOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CeilOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
        assertResolved(CeilOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }


    @Test
    void testArgumentTypes() {
        testValidInput(CeilOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE);
        testInvalidInput(CeilOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_DECIMAL);
        testInvalidInput(CeilOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_DECIMAL);
    }
}
