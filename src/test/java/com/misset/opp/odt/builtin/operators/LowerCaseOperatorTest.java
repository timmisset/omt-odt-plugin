package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LowerCaseOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LowerCaseOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testValidArguments() {
        assertValidInput(LowerCaseOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertInvalidInput(LowerCaseOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LOWERCASE", LowerCaseOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LowerCaseOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, LowerCaseOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(LowerCaseOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }
}
