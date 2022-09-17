package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ConcatOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ConcatOperator.INSTANCE, OntologyModelConstants.getXsdIntegerInstance(), OntologyModelConstants.getXsdStringInstance());
        assertResolved(ConcatOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdStringInstance()),
                Set.of(OntologyModelConstants.getXsdStringInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("CONCAT", ConcatOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, ConcatOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, ConcatOperator.INSTANCE.maxNumberOfArguments());
    }
}
