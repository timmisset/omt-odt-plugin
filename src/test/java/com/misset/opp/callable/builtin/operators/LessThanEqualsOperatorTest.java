package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class LessThanEqualsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LessThanEqualsOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }
}
