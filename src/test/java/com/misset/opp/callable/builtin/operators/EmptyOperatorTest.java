package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class EmptyOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EmptyOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }
}
