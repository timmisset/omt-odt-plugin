package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class LessThanEqualsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LessThanEqualsOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(LessThanEqualsOperator.INSTANCE, 1, OppModelConstants.getXsdStringInstance());
        assertValidArgument(LessThanEqualsOperator.INSTANCE, 1, OppModelConstants.getXsdNumberInstance());
        assertValidArgument(LessThanEqualsOperator.INSTANCE, 1, OppModelConstants.getXsdIntegerInstance());
        assertValidArgument(LessThanEqualsOperator.INSTANCE, 1, OppModelConstants.getXsdDecimalInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("LESS_THAN_EQUALS", LessThanEqualsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, LessThanEqualsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, LessThanEqualsOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(LessThanEqualsOperator.INSTANCE, 0, Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdNumberInstance()));
        assertGetAcceptableArgumentType(LessThanEqualsOperator.INSTANCE, 1, Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdNumberInstance()));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(LessThanEqualsOperator.INSTANCE, Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdNumberInstance()));
    }
}
