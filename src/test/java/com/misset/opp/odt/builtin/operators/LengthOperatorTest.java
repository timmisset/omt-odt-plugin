package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LengthOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LengthOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testValidArguments() {
        assertValidInput(LengthOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertInvalidInput(LengthOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LENGTH", LengthOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LengthOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, LengthOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(LengthOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }
}
