package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TruncOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TakeOperator.INSTANCE,
                Set.of(oppModel.XSD_DATE_INSTANCE),
                Set.of(oppModel.XSD_DATE_INSTANCE));
    }

    @Test
    protected void testResolveDateTime() {
        assertResolved(TakeOperator.INSTANCE,
                Set.of(oppModel.XSD_DATETIME_INSTANCE),
                Set.of(oppModel.XSD_DATETIME_INSTANCE));
    }

}
