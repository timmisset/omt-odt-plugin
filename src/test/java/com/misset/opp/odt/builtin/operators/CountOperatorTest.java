package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CountOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CountOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(CountOperator.INSTANCE,
                0,
                oppModel.XSD_BOOLEAN_INSTANCE,
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
