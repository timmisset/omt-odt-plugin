package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IndexOfOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IndexOfOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("INDEX_OF", IndexOfOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, IndexOfOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, IndexOfOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(IndexOfOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(IndexOfOperator.INSTANCE, 1);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(IndexOfOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertInvalidArgument(IndexOfOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_STRING);
        assertValidInput(IndexOfOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertInvalidInput(IndexOfOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_STRING);
    }
}
