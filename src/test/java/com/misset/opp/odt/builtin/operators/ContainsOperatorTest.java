package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContainsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ContainsOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testArgumentTypes() {
        assertValidInput(ContainsOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertInvalidInput(ContainsOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance(), OntologyValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("CONTAINS", ContainsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, ContainsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, ContainsOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(ContainsOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentType(ContainsOperator.INSTANCE, 1, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(ContainsOperator.INSTANCE, 2);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(ContainsOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetFlags() {
        Assertions.assertEquals(AbstractBuiltInOperator.IGNORE_CASE_FLAG, ContainsOperator.INSTANCE.getFlags());
    }
}
