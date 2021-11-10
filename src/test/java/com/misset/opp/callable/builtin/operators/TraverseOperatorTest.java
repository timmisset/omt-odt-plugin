package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TraverseOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TraverseOperator.INSTANCE,
                Set.of(oppModel.OWL_THING),
                Set.of(oppModel.OWL_THING));
    }

}
