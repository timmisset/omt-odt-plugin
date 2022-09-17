package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TruncOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TruncOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdDateInstance()),
                Set.of(OntologyModelConstants.getXsdDateInstance()));
    }

    @Test
    void testResolveDateTime() {
        assertResolved(TruncOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdDatetimeInstance()),
                Set.of(OntologyModelConstants.getXsdDatetimeInstance()));
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
        assertValidInput(TruncOperator.INSTANCE, OntologyModelConstants.getXsdDatetimeInstance());
        assertValidArgument(TruncOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(TruncOperator.INSTANCE, OntologyModelConstants.getXsdDatetimeInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TruncOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(TruncOperator.INSTANCE, 1);
    }
}
