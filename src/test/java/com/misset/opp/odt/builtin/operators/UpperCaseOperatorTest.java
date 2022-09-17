package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UpperCaseOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(UpperCaseOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testValidArguments() {
        assertValidInput(UpperCaseOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertInvalidInput(UpperCaseOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("UPPERCASE", UpperCaseOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, UpperCaseOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, UpperCaseOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(UpperCaseOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }
}
