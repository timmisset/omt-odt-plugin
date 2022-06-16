package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DivideByOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DivideByOperator.INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE);
        assertResolved(DivideByOperator.INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE);
    }

    @Test
    void testValidInputs() {
        assertValidInput(DivideByOperator.INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE);
        assertValidInput(DivideByOperator.INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertValidInput(DivideByOperator.INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(DivideByOperator.INSTANCE, 0, OppModelConstants.XSD_DECIMAL_INSTANCE);
        assertValidArgument(DivideByOperator.INSTANCE, 0, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertValidArgument(DivideByOperator.INSTANCE, 0, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertInvalidArgument(DivideByOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_NUMBER);
    }

    @Test
    void testName() {
        Assertions.assertEquals("DIVIDE_BY", DivideByOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, DivideByOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, DivideByOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(DivideByOperator.INSTANCE, 0, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(DivideByOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(DivideByOperator.INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
    }

}
