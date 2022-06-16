package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class LessThanOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LessThanOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(LessThanOperator.INSTANCE, 1, OppModelConstants.XSD_STRING_INSTANCE);
        assertValidArgument(LessThanOperator.INSTANCE, 1, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidArgument(LessThanOperator.INSTANCE, 1, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertValidArgument(LessThanOperator.INSTANCE, 1, OppModelConstants.XSD_DECIMAL_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LESS_THAN", LessThanOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, LessThanOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, LessThanOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(LessThanOperator.INSTANCE, 0, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
        assertGetAcceptableArgumentType(LessThanOperator.INSTANCE, 1, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(LessThanOperator.INSTANCE, Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE));
    }
}
