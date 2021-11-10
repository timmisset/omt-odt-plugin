package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class FloorOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FloorOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
        assertResolved(FloorOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }
}
