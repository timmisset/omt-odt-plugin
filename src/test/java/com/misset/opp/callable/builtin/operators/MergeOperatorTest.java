package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MergeOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        // combine input + argument
        assertResolved(MergeOperator.INSTANCE,
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    protected void testResolveTwoOrMoreArguments() {
        // combine input + argument
        assertResolved(MergeOperator.INSTANCE,
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE, oppModel.XSD_DATE_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_DATE_INSTANCE));
    }

}
