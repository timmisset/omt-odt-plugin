package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CeilOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CeilOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance(), OntologyModelConstants.getXsdIntegerInstance());
        assertResolved(CeilOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance(), OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testArgumentTypes() {
        assertValidInput(CeilOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance());
        assertInvalidInput(CeilOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance(), OntologyValidationUtil.ERROR_MESSAGE_DECIMAL);
        assertInvalidInput(CeilOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyValidationUtil.ERROR_MESSAGE_DECIMAL);
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
        assertGetAcceptableInputType(CeilOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance());
    }
}
