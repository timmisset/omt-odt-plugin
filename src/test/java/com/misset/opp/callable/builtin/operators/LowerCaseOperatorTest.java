package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class LowerCaseOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LowerCaseOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }


}
