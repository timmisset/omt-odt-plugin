package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(JsonOperator.INSTANCE, OntologyModelConstants.getJsonObject());
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
