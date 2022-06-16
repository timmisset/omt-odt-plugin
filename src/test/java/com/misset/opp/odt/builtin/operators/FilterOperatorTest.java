package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FilterOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.XSD_DATE, OppModelConstants.XSD_DATE);
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.XSD_INTEGER, OppModelConstants.XSD_INTEGER);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FilterOperator.INSTANCE, 0, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertInvalidArgument(FilterOperator.INSTANCE, 0, OppModelConstants.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FILTER", FilterOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, FilterOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, FilterOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(FilterOperator.INSTANCE, 0, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentType(FilterOperator.INSTANCE, 1, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }
}
