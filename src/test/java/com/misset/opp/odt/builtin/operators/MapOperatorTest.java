package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MapOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MapOperator.INSTANCE,
                Set.of(OppModelConstants.XSD_STRING_INSTANCE),
                Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE),
                Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE));
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
