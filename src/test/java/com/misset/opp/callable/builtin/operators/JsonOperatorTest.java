package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class JsonOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(JsonOperator.INSTANCE, oppModel.JSON_OBJECT);
    }


}
