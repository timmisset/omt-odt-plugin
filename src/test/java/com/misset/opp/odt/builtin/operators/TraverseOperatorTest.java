package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TraverseOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TraverseOperator.INSTANCE,
                Set.of(OppModelConstants.OWL_THING_INSTANCE),
                Set.of(OppModelConstants.OWL_THING_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("TRAVERSE", TraverseOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, TraverseOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, TraverseOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(TraverseOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
        assertValidArgument(TraverseOperator.INSTANCE, 1, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TraverseOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentType(TraverseOperator.INSTANCE, 1, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(TraverseOperator.INSTANCE, 2);
    }
}
