package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class DivideByOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DivideByOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_DECIMAL_INSTANCE);
        assertResolved(DivideByOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, oppModel.XSD_DECIMAL_INSTANCE);
    }
}
