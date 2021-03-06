package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CeilOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CeilOperator.INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertResolved(CeilOperator.INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        assertValidInput(CeilOperator.INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE);
        assertInvalidInput(CeilOperator.INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_DECIMAL);
        assertInvalidInput(CeilOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_DECIMAL);
    }

    @Test
    void testName() {
        Assertions.assertEquals("CEIL", CeilOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, CeilOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, CeilOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(CeilOperator.INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE);
    }
}
