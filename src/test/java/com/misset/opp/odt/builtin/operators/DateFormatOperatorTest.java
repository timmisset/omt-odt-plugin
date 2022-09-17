package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateFormatOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DateFormatOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("FORMAT_DATE", DateFormatOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, DateFormatOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, DateFormatOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        assertValidInput(DateFormatOperator.INSTANCE, OntologyModelConstants.getXsdDateInstance());
        assertValidInput(DateFormatOperator.INSTANCE, OntologyModelConstants.getXsdDatetimeInstance());
        testArgument(DateFormatOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance(), OntologyValidationUtil.ERROR_MESSAGE_STRING);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(DateFormatOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(DateFormatOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(DateFormatOperator.INSTANCE, OntologyModelConstants.getXsdDatetimeInstance());
    }
}
