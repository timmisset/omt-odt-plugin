package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class UpperCaseOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(UpperCaseOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }


}
