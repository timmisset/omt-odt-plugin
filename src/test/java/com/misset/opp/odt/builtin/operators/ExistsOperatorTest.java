package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class ExistsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EveryOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

}
