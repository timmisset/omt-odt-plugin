package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MaxOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MaxOperator.INSTANCE,
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("MAX", MaxOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, MaxOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, MaxOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(MaxOperator.INSTANCE, oppModel.XSD_NUMBER_INSTANCE);
        assertValidInput(MaxOperator.INSTANCE, oppModel.XSD_DATETIME_INSTANCE);
        assertInvalidInput(MaxOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, "Acceptable types:");

        assertValidArgument(MaxOperator.INSTANCE, 0, oppModel.XSD_NUMBER_INSTANCE);
        assertValidArgument(MaxOperator.INSTANCE, 0, oppModel.XSD_DATETIME_INSTANCE);
        assertInvalidArgument(MaxOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE, "Acceptable types:");
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(MaxOperator.INSTANCE, Set.of(oppModel.XSD_NUMBER_INSTANCE, oppModel.XSD_DATETIME_INSTANCE));
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MaxOperator.INSTANCE, 0, Set.of(oppModel.XSD_NUMBER_INSTANCE, oppModel.XSD_DATETIME_INSTANCE));
        assertGetAcceptableArgumentTypeIsNull(MaxOperator.INSTANCE, 1);
    }
}
