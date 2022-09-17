package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SomeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SomeOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testValidInputs() {
        assertValidInput(SomeOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
        assertInvalidInput(SomeOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(SomeOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertInvalidArgument(SomeOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("SOME", SomeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, SomeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, SomeOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(SomeOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(SomeOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SomeOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }
}
