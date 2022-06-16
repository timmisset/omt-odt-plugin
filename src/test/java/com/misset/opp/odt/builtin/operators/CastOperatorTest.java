package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class CastOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CastOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE, Set.of(OppModelConstants.XSD_INTEGER));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_DECIMAL_INSTANCE, Set.of(OppModelConstants.XSD_DECIMAL));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_DATETIME_INSTANCE, Set.of(OppModelConstants.XSD_DATETIME));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_DATE_INSTANCE, Set.of(OppModelConstants.XSD_DATE));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE, Set.of(OppModelConstants.XSD_BOOLEAN));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.OWL_THING_INSTANCE, Set.of(IriOperator.INSTANCE.resolveSingle()));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.JSON_OBJECT, Set.of(JsonOperator.INSTANCE.resolveSingle()));
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
