package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ConcatOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ConcatOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_STRING_INSTANCE);
        assertResolved(ConcatOperator.INSTANCE,
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("CONCAT", ConcatOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, ConcatOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, ConcatOperator.INSTANCE.maxNumberOfArguments());
    }
}
