package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class IdentityOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IdentityOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
        assertResolved(IdentityOperator.INSTANCE, oppModel.XSD_DATE, oppModel.XSD_DATE);
        assertResolved(IdentityOperator.INSTANCE, oppModel.XSD_INTEGER, oppModel.XSD_INTEGER);
    }
}
