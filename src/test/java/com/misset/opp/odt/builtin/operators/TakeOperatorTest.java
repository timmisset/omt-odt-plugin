package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TakeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TakeOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("TAKE", TakeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, TakeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, TakeOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(TakeOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TakeOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance());
        assertGetAcceptableArgumentTypeIsNull(TakeOperator.INSTANCE, 1);
    }
}
