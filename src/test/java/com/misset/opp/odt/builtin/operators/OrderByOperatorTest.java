package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OrderByOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(OrderByOperator.INSTANCE,
                Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE),
                Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("ORDER_BY", OrderByOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, OrderByOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, OrderByOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(OrderByOperator.INSTANCE, 1, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }
}
