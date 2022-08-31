package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonValuesOperatorTest extends BaseBuiltinTest {
    @Override
    @Test
    protected void testResolve() {
        assertResolved(JsonValuesOperator.INSTANCE, OppModelConstants.getOwlThingInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("JSON_VALUES", JsonValuesOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, JsonValuesOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, JsonValuesOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(JsonValuesOperator.INSTANCE, OppModelConstants.getJsonObject());
        assertInvalidInput(JsonValuesOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance(), TTLValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableInputType(JsonValuesOperator.INSTANCE, OppModelConstants.getJsonObject());
    }
}
