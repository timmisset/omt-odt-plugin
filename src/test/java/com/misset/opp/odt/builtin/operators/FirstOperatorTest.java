package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FirstOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FirstOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyModelConstants.getXsdBooleanInstance());
        assertResolved(FirstOperator.INSTANCE, OntologyModelConstants.getXsdDate(), OntologyModelConstants.getXsdDate());
        assertResolved(FirstOperator.INSTANCE, OntologyModelConstants.getXsdInteger(), OntologyModelConstants.getXsdInteger());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FirstOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertInvalidArgument(FirstOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FIRST", FirstOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, FirstOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, FirstOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(FirstOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(FirstOperator.INSTANCE, 1);
    }
}
