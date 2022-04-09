package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TypeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TypeOperator.INSTANCE,
                Set.of(oppModel.XSD_DATE_INSTANCE),
                Set.of(oppModel.XSD_DATE));

        assertResolved(TypeOperator.INSTANCE,
                Set.of(oppModel.MEDEWERKER_GRAPH),
                Set.of(oppModel.IRI));
    }

    @Test
    void testName() {
        Assertions.assertEquals("TYPE", TypeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, TypeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, TypeOperator.INSTANCE.maxNumberOfArguments());
    }
}
