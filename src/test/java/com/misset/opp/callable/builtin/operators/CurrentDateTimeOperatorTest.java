package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class CurrentDateTimeOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CurrentDateTimeOperator.INSTANCE, oppModel.XSD_DATETIME_INSTANCE);
    }
}
