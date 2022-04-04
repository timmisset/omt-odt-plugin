package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

class DateFormatOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DateFormatOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        assertValidInput(DateFormatOperator.INSTANCE, oppModel.XSD_DATE_INSTANCE);
        assertValidInput(DateFormatOperator.INSTANCE, oppModel.XSD_DATETIME_INSTANCE);
        testArgument(DateFormatOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_STRING);
    }
}
