package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(JsonOperator.INSTANCE, OppModelConstants.getJsonObject());
    }

    @Test
    void testName() {
        Assertions.assertEquals("JSON", JsonOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, JsonOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, JsonOperator.INSTANCE.maxNumberOfArguments());
    }

}
