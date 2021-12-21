package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ConcatOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ConcatOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_STRING_INSTANCE);
        assertResolved(ConcatOperator.INSTANCE,
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE));
    }
}
