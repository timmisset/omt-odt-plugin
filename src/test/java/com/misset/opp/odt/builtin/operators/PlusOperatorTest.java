package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class PlusOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(PlusOperator.INSTANCE,
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_INTEGER_INSTANCE)
        );
    }

    @Test
    protected void testResolveDecimal() {
        assertResolved(PlusOperator.INSTANCE,
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_DECIMAL_INSTANCE),
                Set.of(oppModel.XSD_DECIMAL_INSTANCE)
        );
    }

    @Test
    protected void testResolveDecimalLeading() {
        assertResolved(PlusOperator.INSTANCE,
                Set.of(oppModel.XSD_DECIMAL_INSTANCE),
                Set.of(oppModel.XSD_DECIMAL_INSTANCE),
                Set.of(oppModel.XSD_INTEGER_INSTANCE)
        );
    }

    @Test
    void testName() {
        Assertions.assertEquals("PLUS", PlusOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, PlusOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, PlusOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        BuiltinMathOperator.validInputs.forEach(
                resource -> assertValidInput(PlusOperator.INSTANCE, resource)
        );
        BuiltinMathOperator.validInputs.forEach(
                resource -> assertValidArgument(PlusOperator.INSTANCE, 0, resource)
        );
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(PlusOperator.INSTANCE, BuiltinMathOperator.validInputs);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(PlusOperator.INSTANCE, 0, BuiltinMathOperator.validInputs);
        assertGetAcceptableArgumentType(PlusOperator.INSTANCE, 1, BuiltinMathOperator.validInputs);
    }
}
