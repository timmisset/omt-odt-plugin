package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class GreaterThanOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GreaterThanOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }
}