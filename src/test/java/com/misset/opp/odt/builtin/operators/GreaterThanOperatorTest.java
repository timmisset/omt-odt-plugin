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
        assertResolved(GreaterThanOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OppModelConstants.XSD_STRING_INSTANCE);
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertValidArgument(GreaterThanOperator.INSTANCE, 1, OppModelConstants.XSD_DECIMAL_INSTANCE);
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
        assertGetAcceptableArgumentType(GreaterThanOperator.INSTANCE, 0, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
        assertGetAcceptableArgumentType(GreaterThanOperator.INSTANCE, 1, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(GreaterThanOperator.INSTANCE, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
    }
}
