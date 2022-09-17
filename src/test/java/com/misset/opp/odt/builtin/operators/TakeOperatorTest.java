package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TakeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TakeOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdBooleanInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("TAKE", TakeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, TakeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, TakeOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(TakeOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TakeOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
        assertGetAcceptableArgumentTypeIsNull(TakeOperator.INSTANCE, 1);
    }
}
