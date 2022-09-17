package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CountOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CountOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testArgumentTypes() {
        testArgument(CountOperator.INSTANCE,
                0,
                OntologyModelConstants.getXsdBooleanInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
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
