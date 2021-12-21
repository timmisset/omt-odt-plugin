package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

class GreaterThanEqualsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GreaterThanEqualsOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidArguments() {
        testValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, oppModel.XSD_STRING_INSTANCE);
        testValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, oppModel.XSD_NUMBER_INSTANCE);
        testValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, oppModel.XSD_INTEGER_INSTANCE);
        testValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, oppModel.XSD_DECIMAL_INSTANCE);
    }
}
