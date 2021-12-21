package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SumOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SumOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
        assertResolved(SumOperator.INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, oppModel.XSD_DECIMAL_INSTANCE);
        assertResolved(SumOperator.INSTANCE, Set.of(oppModel.XSD_INTEGER_INSTANCE, oppModel.XSD_DECIMAL_INSTANCE), Set.of(oppModel.XSD_DECIMAL_INSTANCE));
    }


}
