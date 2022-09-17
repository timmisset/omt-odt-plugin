package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class CastOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(CastOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdIntegerInstance(), Set.of(OntologyModelConstants.getXsdInteger()));
        assertResolved(CastOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdDecimalInstance(), Set.of(OntologyModelConstants.getXsdDecimal()));
        assertResolved(CastOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdDatetimeInstance(), Set.of(OntologyModelConstants.getXsdDatetime()));
        assertResolved(CastOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdDateInstance(), Set.of(OntologyModelConstants.getXsdDate()));
        assertResolved(CastOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdBooleanInstance(), Set.of(OntologyModelConstants.getXsdBoolean()));
        assertResolved(CastOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getOwlThingInstance(), Set.of(IriOperator.INSTANCE.resolveSingle()));
        assertResolved(CastOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getJsonObject(), Set.of(JsonOperator.INSTANCE.resolveSingle()));
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
