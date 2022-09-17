package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonValuesOperatorTest extends BaseBuiltinTest {
    @Override
    @Test
    protected void testResolve() {
        assertResolved(JsonValuesOperator.INSTANCE, OntologyModelConstants.getOwlThingInstance());
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
        assertValidInput(JsonValuesOperator.INSTANCE, OntologyModelConstants.getJsonObject());
        assertInvalidInput(JsonValuesOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableInputType(JsonValuesOperator.INSTANCE, OntologyModelConstants.getJsonObject());
    }
}
