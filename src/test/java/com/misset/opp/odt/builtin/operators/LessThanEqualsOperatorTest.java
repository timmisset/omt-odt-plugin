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
        assertResolved(LessThanEqualsOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(LessThanEqualsOperator.INSTANCE, 1, OppModelConstants.XSD_STRING_INSTANCE);
        assertValidArgument(LessThanEqualsOperator.INSTANCE, 1, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidArgument(LessThanEqualsOperator.INSTANCE, 1, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertValidArgument(LessThanEqualsOperator.INSTANCE, 1, OppModelConstants.XSD_DECIMAL_INSTANCE);
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
        assertGetAcceptableArgumentType(LessThanEqualsOperator.INSTANCE, 0, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
        assertGetAcceptableArgumentType(LessThanEqualsOperator.INSTANCE, 1, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(LessThanEqualsOperator.INSTANCE, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
    }
}
