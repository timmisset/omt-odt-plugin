package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TrimOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TrimOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("TRIM", TrimOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, TrimOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, TrimOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(TrimOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(TrimOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }
}
