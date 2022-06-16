package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TruncOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TruncOperator.INSTANCE,
                Set.of(OppModelConstants.XSD_DATE_INSTANCE),
                Set.of(OppModelConstants.XSD_DATE_INSTANCE));
    }

    @Test
    protected void testResolveDateTime() {
        assertResolved(TruncOperator.INSTANCE,
                Set.of(OppModelConstants.XSD_DATETIME_INSTANCE),
                Set.of(OppModelConstants.XSD_DATETIME_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("TRUNC", TruncOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, TruncOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, TruncOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(TruncOperator.INSTANCE, OppModelConstants.XSD_DATETIME_INSTANCE);
        assertValidArgument(TruncOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(TruncOperator.INSTANCE, OppModelConstants.XSD_DATETIME_INSTANCE);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TruncOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(TruncOperator.INSTANCE, 1);
    }
}
