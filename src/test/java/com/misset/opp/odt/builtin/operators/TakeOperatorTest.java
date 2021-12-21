package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TakeOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TakeOperator.INSTANCE,
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

}
