package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExistsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ExistsOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("EXISTS", ExistsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, ExistsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, ExistsOperator.INSTANCE.maxNumberOfArguments());
    }
}
