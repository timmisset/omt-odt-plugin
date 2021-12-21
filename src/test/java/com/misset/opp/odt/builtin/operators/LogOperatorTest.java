package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class LogOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LogOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
        assertResolved(LogOperator.INSTANCE, oppModel.XSD_DATE, oppModel.XSD_DATE);
        assertResolved(LogOperator.INSTANCE, oppModel.XSD_INTEGER, oppModel.XSD_INTEGER);
    }
}
