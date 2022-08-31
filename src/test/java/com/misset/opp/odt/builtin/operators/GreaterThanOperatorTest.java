package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class GreaterThanOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GreaterThanOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OppModelConstants.getXsdStringInstance());
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OppModelConstants.getXsdNumberInstance());
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OppModelConstants.getXsdIntegerInstance());
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OppModelConstants.getXsdDecimalInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("GREATER_THAN", GreaterThanOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, GreaterThanOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, GreaterThanOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(GreaterThanOperator.INSTANCE, 0, Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdNumberInstance()));
        assertGetAcceptableArgumentType(GreaterThanOperator.INSTANCE, 1, Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdNumberInstance()));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(GreaterThanOperator.INSTANCE, Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdNumberInstance()));
    }
}
