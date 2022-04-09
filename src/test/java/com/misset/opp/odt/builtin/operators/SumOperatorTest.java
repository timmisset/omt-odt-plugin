package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SumOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SumOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
        assertResolved(SumOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, oppModel.XSD_DECIMAL_INSTANCE);
        assertResolved(SumOperator.INSTANCE, Set.of(oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_DECIMAL_INSTANCE), Set.of(oppModel.XSD_DECIMAL_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("SUM", SumOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, SumOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, SumOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(SumOperator.INSTANCE, oppModel.XSD_NUMBER_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SumOperator.INSTANCE, oppModel.XSD_NUMBER_INSTANCE);
    }
}
