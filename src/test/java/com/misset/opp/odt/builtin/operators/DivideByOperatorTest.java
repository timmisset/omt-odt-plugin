package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DivideByOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DivideByOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance(), OntologyModelConstants.getXsdDecimalInstance());
        assertResolved(DivideByOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance(), OntologyModelConstants.getXsdDecimalInstance());
    }

    @Test
    void testValidInputs() {
        assertValidInput(DivideByOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance());
        assertValidInput(DivideByOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance());
        assertValidInput(DivideByOperator.INSTANCE, OntologyModelConstants.getXsdNumberInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(DivideByOperator.INSTANCE, 0, OntologyModelConstants.getXsdDecimalInstance());
        assertValidArgument(DivideByOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
        assertValidArgument(DivideByOperator.INSTANCE, 0, OntologyModelConstants.getXsdNumberInstance());
        assertInvalidArgument(DivideByOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance(), OntologyValidationUtil.ERROR_MESSAGE_NUMBER);
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
        assertGetAcceptableArgumentType(DivideByOperator.INSTANCE, 0, OntologyModelConstants.getXsdNumberInstance());
        assertGetAcceptableArgumentTypeIsNull(DivideByOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(DivideByOperator.INSTANCE, OntologyModelConstants.getXsdNumberInstance());
    }

}
