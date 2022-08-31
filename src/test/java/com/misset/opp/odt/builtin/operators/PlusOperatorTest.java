package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class PlusOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(PlusOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdIntegerInstance()),
                Set.of(OppModelConstants.getXsdIntegerInstance()),
                Set.of(OppModelConstants.getXsdIntegerInstance())
        );
    }

    @Test
    protected void testResolveDecimal() {
        assertResolved(PlusOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdIntegerInstance()),
                Set.of(OppModelConstants.getXsdDecimalInstance()),
                Set.of(OppModelConstants.getXsdDecimalInstance())
        );
    }

    @Test
    protected void testResolveDecimalLeading() {
        assertResolved(PlusOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdDecimalInstance()),
                Set.of(OppModelConstants.getXsdDecimalInstance()),
                Set.of(OppModelConstants.getXsdIntegerInstance())
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
