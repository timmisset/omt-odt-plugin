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
        assertResolved(CastOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdIntegerInstance(), Set.of(OppModelConstants.getXsdInteger()));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdDecimalInstance(), Set.of(OppModelConstants.getXsdDecimal()));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdDatetimeInstance(), Set.of(OppModelConstants.getXsdDatetime()));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdDateInstance(), Set.of(OppModelConstants.getXsdDate()));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdBooleanInstance(), Set.of(OppModelConstants.getXsdBoolean()));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), OppModelConstants.getOwlThingInstance(), Set.of(IriOperator.INSTANCE.resolveSingle()));
        assertResolved(CastOperator.INSTANCE, OppModelConstants.getXsdStringInstance(), OppModelConstants.getJsonObject(), Set.of(JsonOperator.INSTANCE.resolveSingle()));
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
