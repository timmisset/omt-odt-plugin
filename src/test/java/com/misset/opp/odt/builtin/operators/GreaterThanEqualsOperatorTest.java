package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class GreaterThanEqualsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GreaterThanEqualsOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, OppModelConstants.XSD_STRING_INSTANCE);
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, OppModelConstants.XSD_DECIMAL_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("GREATER_THAN_EQUALS", GreaterThanEqualsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, GreaterThanEqualsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, GreaterThanEqualsOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(GreaterThanEqualsOperator.INSTANCE, 0, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
        assertGetAcceptableArgumentType(GreaterThanEqualsOperator.INSTANCE, 1, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(GreaterThanEqualsOperator.INSTANCE, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
    }
}
