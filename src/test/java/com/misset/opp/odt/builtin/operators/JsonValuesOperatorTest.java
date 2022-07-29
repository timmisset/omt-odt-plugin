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
        assertResolved(JsonValuesOperator.INSTANCE, OppModelConstants.OWL_THING_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("JSONVALUES", JsonValuesOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, JsonValuesOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(0, JsonValuesOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(JsonValuesOperator.INSTANCE, OppModelConstants.JSON_OBJECT);
        assertInvalidInput(JsonValuesOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableInputType(JsonValuesOperator.INSTANCE, OppModelConstants.JSON_OBJECT);
    }
}
