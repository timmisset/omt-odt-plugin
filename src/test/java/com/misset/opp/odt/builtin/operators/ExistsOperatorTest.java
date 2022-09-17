package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExistsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ExistsOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
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
