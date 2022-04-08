package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.OppModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class LessThanOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(LessThanOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testValidArguments() {
        testValidArgument(LessThanOperator.INSTANCE, 1, oppModel.XSD_STRING_INSTANCE);
        testValidArgument(LessThanOperator.INSTANCE, 1, oppModel.XSD_NUMBER_INSTANCE);
        testValidArgument(LessThanOperator.INSTANCE, 1, oppModel.XSD_INTEGER_INSTANCE);
        testValidArgument(LessThanOperator.INSTANCE, 1, oppModel.XSD_DECIMAL_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("LESS_THAN", LessThanOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, LessThanOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, LessThanOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(LessThanOperator.INSTANCE, 0, Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE, OppModel.INSTANCE.XSD_NUMBER_INSTANCE));
        assertGetAcceptableArgumentType(LessThanOperator.INSTANCE, 1, Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE, OppModel.INSTANCE.XSD_NUMBER_INSTANCE));
    }

    @Test
    void testGetAcceptableInput() {
        assertGetAcceptableInputType(LessThanOperator.INSTANCE, Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE, OppModel.INSTANCE.XSD_NUMBER_INSTANCE));
    }
}
