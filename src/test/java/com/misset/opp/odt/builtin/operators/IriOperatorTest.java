package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IriOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IriOperator.INSTANCE, oppModel.IRI);
    }

    @Test
    void testName() {
        Assertions.assertEquals("IRI", IriOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, IriOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, IriOperator.INSTANCE.maxNumberOfArguments());
    }

}
