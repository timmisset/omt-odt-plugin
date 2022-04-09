package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MinOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MinOperator.INSTANCE,
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("MIN", MinOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, MinOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, MinOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(MinOperator.INSTANCE, oppModel.XSD_NUMBER_INSTANCE);
        assertValidInput(MinOperator.INSTANCE, oppModel.XSD_DATETIME_INSTANCE);
        assertInvalidInput(MinOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, "Acceptable types:");

        assertValidArgument(MinOperator.INSTANCE, 0, oppModel.XSD_NUMBER_INSTANCE);
        assertValidArgument(MinOperator.INSTANCE, 0, oppModel.XSD_DATETIME_INSTANCE);
        assertInvalidArgument(MinOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE, "Acceptable types:");
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(MinOperator.INSTANCE, Set.of(oppModel.XSD_NUMBER_INSTANCE, oppModel.XSD_DATETIME_INSTANCE));
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MinOperator.INSTANCE, 0, Set.of(oppModel.XSD_NUMBER_INSTANCE, oppModel.XSD_DATETIME_INSTANCE));
        assertGetAcceptableArgumentTypeIsNull(MinOperator.INSTANCE, 1);
    }
}
