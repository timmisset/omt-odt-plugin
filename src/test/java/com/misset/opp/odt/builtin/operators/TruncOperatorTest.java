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
                Set.of(OppModelConstants.getXsdDateInstance()),
                Set.of(OppModelConstants.getXsdDateInstance()));
    }

    @Test
    protected void testResolveDateTime() {
        assertResolved(TruncOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdDatetimeInstance()),
                Set.of(OppModelConstants.getXsdDatetimeInstance()));
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
        assertValidInput(TruncOperator.INSTANCE, OppModelConstants.getXsdDatetimeInstance());
        assertValidArgument(TruncOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(TruncOperator.INSTANCE, OppModelConstants.getXsdDatetimeInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TruncOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(TruncOperator.INSTANCE, 1);
    }
}
