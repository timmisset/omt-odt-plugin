package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MapOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(MapOperator.INSTANCE,
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

}
