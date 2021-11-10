package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class IndexOfOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IndexOfOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }
}
