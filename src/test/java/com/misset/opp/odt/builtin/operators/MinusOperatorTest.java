package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MinusOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MinusOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance())
        );
    }

    @Test
    void testResolveDecimal() {
        assertResolved(MinusOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdDecimalInstance()),
                Set.of(OntologyModelConstants.getXsdDecimalInstance())
        );
    }

    @Test
    void testResolveDecimalLeading() {
        assertResolved(MinusOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdDecimalInstance()),
                Set.of(OntologyModelConstants.getXsdDecimalInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance())
        );
    }

    @Test
    void testResolveMoreThan2Arguments() {
        assertResolved(MinusOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance())
        );
    }

    @Test
    void testName() {
        Assertions.assertEquals("MINUS", MinusOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, MinusOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, MinusOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        BuiltinMathOperator.validInputs.forEach(
                resource -> assertValidInput(MinusOperator.INSTANCE, resource)
        );
        BuiltinMathOperator.validInputs.forEach(
                resource -> assertValidArgument(MinusOperator.INSTANCE, 0, resource)
        );
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(MinusOperator.INSTANCE, BuiltinMathOperator.validInputs);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(MinusOperator.INSTANCE, 0, BuiltinMathOperator.validInputs);
        assertGetAcceptableArgumentType(MinusOperator.INSTANCE, 1, BuiltinMathOperator.validInputs);
    }
}
