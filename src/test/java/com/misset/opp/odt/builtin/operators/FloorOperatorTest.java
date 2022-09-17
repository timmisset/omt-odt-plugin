package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FloorOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FloorOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance(), OntologyModelConstants.getXsdIntegerInstance());
        assertResolved(FloorOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance(), OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testValidInputs() {
        assertValidInput(FloorOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance());
        assertInvalidInput(FloorOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyValidationUtil.ERROR_MESSAGE_DECIMAL);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FLOOR", FloorOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, FloorOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, FloorOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(FloorOperator.INSTANCE, OntologyModelConstants.getXsdDecimalInstance());
    }
}
