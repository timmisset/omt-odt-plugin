package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FloorOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FloorOperator.INSTANCE, OppModelConstants.getXsdDecimalInstance(), OppModelConstants.getXsdIntegerInstance());
        assertResolved(FloorOperator.INSTANCE, OppModelConstants.getXsdIntegerInstance(), OppModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testValidInputs() {
        assertValidInput(FloorOperator.INSTANCE, OppModelConstants.getXsdDecimalInstance());
        assertInvalidInput(FloorOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), TTLValidationUtil.ERROR_MESSAGE_DECIMAL);
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
        assertGetAcceptableInputType(FloorOperator.INSTANCE, OppModelConstants.getXsdDecimalInstance());
    }
}
