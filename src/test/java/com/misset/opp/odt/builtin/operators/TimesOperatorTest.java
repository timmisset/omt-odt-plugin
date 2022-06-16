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
                Set.of(OppModelConstants.XSD_INTEGER_INSTANCE),
                Set.of(OppModelConstants.XSD_INTEGER_INSTANCE),
                Set.of(OppModelConstants.XSD_INTEGER_INSTANCE)
        );
    }

    @Test
    protected void testResolveDecimal() {
        assertResolved(TimesOperator.INSTANCE,
                Set.of(OppModelConstants.XSD_INTEGER_INSTANCE),
                Set.of(OppModelConstants.XSD_DECIMAL_INSTANCE),
                Set.of(OppModelConstants.XSD_DECIMAL_INSTANCE)
        );
    }

    @Test
    protected void testResolveDecimalLeading() {
        assertResolved(TimesOperator.INSTANCE,
                Set.of(OppModelConstants.XSD_DECIMAL_INSTANCE),
                Set.of(OppModelConstants.XSD_DECIMAL_INSTANCE),
                Set.of(OppModelConstants.XSD_INTEGER_INSTANCE)
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
        assertValidInput(TimesOperator.INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidArgument(TimesOperator.INSTANCE, 0, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidArgument(TimesOperator.INSTANCE, 1, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertValidArgument(TimesOperator.INSTANCE, 2, OppModelConstants.XSD_NUMBER_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(TimesOperator.INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(TimesOperator.INSTANCE, 0, OppModelConstants.XSD_NUMBER_INSTANCE);
        assertGetAcceptableArgumentType(TimesOperator.INSTANCE, 1, OppModelConstants.XSD_NUMBER_INSTANCE);
    }

}
