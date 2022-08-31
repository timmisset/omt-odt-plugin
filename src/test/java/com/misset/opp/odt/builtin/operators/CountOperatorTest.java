package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CountOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CountOperator.INSTANCE, OppModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testArgumentTypes() {
        testArgument(CountOperator.INSTANCE,
                0,
                OppModelConstants.getXsdBooleanInstance(),
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("COUNT", CountOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, CountOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, CountOperator.INSTANCE.maxNumberOfArguments());
    }
}
