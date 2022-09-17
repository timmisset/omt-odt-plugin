package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MapOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MapOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdStringInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("MAP", MapOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, MapOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, MapOperator.INSTANCE.maxNumberOfArguments());
    }
}
