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
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance(), OppModelConstants.getXsdBooleanInstance());
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.getXsdDate(), OppModelConstants.getXsdDate());
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.getXsdInteger(), OppModelConstants.getXsdInteger());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FilterOperator.INSTANCE, 0, OppModelConstants.getXsdBooleanInstance());
        assertInvalidArgument(FilterOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance(), TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
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
        assertGetAcceptableArgumentType(FilterOperator.INSTANCE, 0, OppModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentType(FilterOperator.INSTANCE, 1, OppModelConstants.getXsdBooleanInstance());
    }
}
