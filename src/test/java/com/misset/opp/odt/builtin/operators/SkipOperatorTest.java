package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SkipOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SkipOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("SKIP", SkipOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, SkipOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, SkipOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(SkipOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(SkipOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance());
        assertGetAcceptableArgumentTypeIsNull(SkipOperator.INSTANCE, 1);
    }

}
