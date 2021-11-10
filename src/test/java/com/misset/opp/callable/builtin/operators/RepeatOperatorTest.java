package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class RepeatOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        // no known error state, return input + catch
        assertResolved(IfEmptyOperator.INSTANCE,
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE,
                        oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

}
