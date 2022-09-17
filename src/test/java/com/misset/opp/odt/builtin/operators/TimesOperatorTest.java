package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TimesOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TimesOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance())
        );
    }

    @Test
    void testResolveDecimal() {
        assertResolved(TimesOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdIntegerInstance()),
                Set.of(OntologyModelConstants.getXsdDecimalInstance()),
                Set.of(OntologyModelConstants.getXsdDecimalInstance())
        );
    }

    @Test
    void testResolveDecimalLeading() {
        assertResolved(TimesOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdDecimalInstance()),
                Set.of(OntologyModelConstants.getXsdDecimalInstance()),
                Set.of(OntologyModelConstants.getXsdIntegerInstance())
        );
    }

    @Test
    void testName() {
        Assertions.assertEquals("TIMES", TimesOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, TimesOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, TimesOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(TimesOperator.INSTANCE, OntologyModelConstants.getXsdNumberInstance());
        assertValidArgument(TimesOperator.INSTANCE, 0, OntologyModelConstants.getXsdNumberInstance());
        assertValidArgument(TimesOperator.INSTANCE, 1, OntologyModelConstants.getXsdNumberInstance());
        assertValidArgument(TimesOperator.INSTANCE, 2, OntologyModelConstants.getXsdNumberInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(TimesOperator.INSTANCE, OntologyModelConstants.getXsdNumberInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TimesOperator.INSTANCE, 0, OntologyModelConstants.getXsdNumberInstance());
        assertGetAcceptableArgumentType(TimesOperator.INSTANCE, 1, OntologyModelConstants.getXsdNumberInstance());
    }

}
