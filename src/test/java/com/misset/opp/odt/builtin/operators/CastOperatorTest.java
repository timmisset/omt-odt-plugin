package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class CastOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CastOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, oppModel.XSD_INTEGER_INSTANCE, Set.of(oppModel.XSD_INTEGER));
        assertResolved(CastOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, oppModel.XSD_DECIMAL_INSTANCE, Set.of(oppModel.XSD_DECIMAL));
        assertResolved(CastOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, oppModel.XSD_DATETIME_INSTANCE, Set.of(oppModel.XSD_DATETIME));
        assertResolved(CastOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, oppModel.XSD_DATE_INSTANCE, Set.of(oppModel.XSD_DATE));
        assertResolved(CastOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, Set.of(oppModel.XSD_BOOLEAN));
        assertResolved(CastOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, oppModel.OWL_THING_INSTANCE, Set.of(IriOperator.INSTANCE.resolveSingle()));
        assertResolved(CastOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE, oppModel.JSON_OBJECT, Set.of(JsonOperator.INSTANCE.resolveSingle()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("CAST", CastOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, CastOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, CastOperator.INSTANCE.maxNumberOfArguments());
    }

}
