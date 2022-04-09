package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class RepeatOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        // no known error state, return input + catch
        assertResolved(RepeatOperator.INSTANCE,
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE,
                        oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("REPEAT", RepeatOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, RepeatOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, RepeatOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(RepeatOperator.INSTANCE, 1, oppModel.XSD_NUMBER_INSTANCE);
        assertValidArgument(RepeatOperator.INSTANCE, 2, oppModel.XSD_NUMBER_INSTANCE);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(RepeatOperator.INSTANCE, 1, oppModel.XSD_NUMBER_INSTANCE);
        assertGetAcceptableArgumentType(RepeatOperator.INSTANCE, 2, oppModel.XSD_NUMBER_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(RepeatOperator.INSTANCE, 0);
    }
}
