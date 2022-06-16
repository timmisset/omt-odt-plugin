package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SumOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SumOperator.INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertResolved(SumOperator.INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE);
        assertResolved(SumOperator.INSTANCE, Set.of(OppModelConstants.XSD_INTEGER_INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE), Set.of(OppModelConstants.XSD_DECIMAL_INSTANCE));
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
        assertValidInput(SumOperator.INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SumOperator.INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
    }
}
