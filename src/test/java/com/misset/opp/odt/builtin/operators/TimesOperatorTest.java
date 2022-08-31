package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TimesOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TimesOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdIntegerInstance()),
                Set.of(OppModelConstants.getXsdIntegerInstance()),
                Set.of(OppModelConstants.getXsdIntegerInstance())
        );
    }

    @Test
    protected void testResolveDecimal() {
        assertResolved(TimesOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdIntegerInstance()),
                Set.of(OppModelConstants.getXsdDecimalInstance()),
                Set.of(OppModelConstants.getXsdDecimalInstance())
        );
    }

    @Test
    protected void testResolveDecimalLeading() {
        assertResolved(TimesOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdDecimalInstance()),
                Set.of(OppModelConstants.getXsdDecimalInstance()),
                Set.of(OppModelConstants.getXsdIntegerInstance())
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
        assertValidInput(TimesOperator.INSTANCE, OppModelConstants.getXsdNumberInstance());
        assertValidArgument(TimesOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
        assertValidArgument(TimesOperator.INSTANCE, 1, OppModelConstants.getXsdNumberInstance());
        assertValidArgument(TimesOperator.INSTANCE, 2, OppModelConstants.getXsdNumberInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(TimesOperator.INSTANCE, OppModelConstants.getXsdNumberInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TimesOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
        assertGetAcceptableArgumentType(TimesOperator.INSTANCE, 1, OppModelConstants.getXsdNumberInstance());
    }

}
