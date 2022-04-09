package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(JsonOperator.INSTANCE, oppModel.JSON_OBJECT);
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
