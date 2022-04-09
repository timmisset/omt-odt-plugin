package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TraverseOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TraverseOperator.INSTANCE,
                Set.of(oppModel.OWL_THING_INSTANCE),
                Set.of(oppModel.OWL_THING_INSTANCE));
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
        assertValidArgument(TraverseOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertValidArgument(TraverseOperator.INSTANCE, 1, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TraverseOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentType(TraverseOperator.INSTANCE, 1, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(TraverseOperator.INSTANCE, 2);
    }
}
