package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class GreaterThanEqualsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GreaterThanEqualsOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }
}
