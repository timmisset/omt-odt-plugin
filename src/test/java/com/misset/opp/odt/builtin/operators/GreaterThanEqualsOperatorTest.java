package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.OppModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class GreaterThanEqualsOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(GreaterThanEqualsOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, oppModel.XSD_STRING_INSTANCE);
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, oppModel.XSD_NUMBER_INSTANCE);
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, oppModel.XSD_INTEGER_INSTANCE);
        assertValidArgument(GreaterThanEqualsOperator.INSTANCE, 1, oppModel.XSD_DECIMAL_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("GREATER_THAN_EQUALS", GreaterThanEqualsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, GreaterThanEqualsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, GreaterThanEqualsOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(GreaterThanEqualsOperator.INSTANCE, 0, Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE, OppModel.INSTANCE.XSD_NUMBER_INSTANCE));
        assertGetAcceptableArgumentType(GreaterThanEqualsOperator.INSTANCE, 1, Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE, OppModel.INSTANCE.XSD_NUMBER_INSTANCE));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(GreaterThanEqualsOperator.INSTANCE, Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE, OppModel.INSTANCE.XSD_NUMBER_INSTANCE));
    }
}
