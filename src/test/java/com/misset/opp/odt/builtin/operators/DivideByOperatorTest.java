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
        assertResolved(DivideByOperator.INSTANCE, OppModelConstants.getXsdIntegerInstance(), OppModelConstants.getXsdDecimalInstance());
        assertResolved(DivideByOperator.INSTANCE, OppModelConstants.getXsdDecimalInstance(), OppModelConstants.getXsdDecimalInstance());
    }

    @Test
    void testValidInputs() {
        assertValidInput(DivideByOperator.INSTANCE, OppModelConstants.getXsdDecimalInstance());
        assertValidInput(DivideByOperator.INSTANCE, OppModelConstants.getXsdIntegerInstance());
        assertValidInput(DivideByOperator.INSTANCE, OppModelConstants.getXsdNumberInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(DivideByOperator.INSTANCE, 0, OppModelConstants.getXsdDecimalInstance());
        assertValidArgument(DivideByOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance());
        assertValidArgument(DivideByOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
        assertInvalidArgument(DivideByOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance(), TTLValidationUtil.ERROR_MESSAGE_NUMBER);
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
        assertGetAcceptableArgumentType(DivideByOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
        assertGetAcceptableArgumentTypeIsNull(DivideByOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(DivideByOperator.INSTANCE, OppModelConstants.getXsdNumberInstance());
    }

}
