package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LastOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LastOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyModelConstants.getXsdBooleanInstance());
        assertResolved(LastOperator.INSTANCE, OntologyModelConstants.getXsdDate(), OntologyModelConstants.getXsdDate());
        assertResolved(LastOperator.INSTANCE, OntologyModelConstants.getXsdInteger(), OntologyModelConstants.getXsdInteger());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(LastOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertInvalidArgument(LastOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LAST", LastOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, LastOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, LastOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(LastOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(LastOperator.INSTANCE, 1);
    }
}
