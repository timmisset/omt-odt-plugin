package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class CurrentDateOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CurrentDateOperator.INSTANCE, oppModel.XSD_DATE_INSTANCE);
    }
}
