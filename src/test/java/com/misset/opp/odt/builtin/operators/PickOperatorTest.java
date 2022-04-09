package com.misset.opp.odt.builtin.operators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PickOperatorTest extends BuiltInOperatorTest {

    @Test
    void testName() {
        Assertions.assertEquals("PICK", PickOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, PickOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, PickOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(PickOperator.INSTANCE, 0, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(PickOperator.INSTANCE, 0, oppModel.XSD_INTEGER_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(PickOperator.INSTANCE, 1);
    }
}
