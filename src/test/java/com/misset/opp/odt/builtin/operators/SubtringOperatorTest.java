package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtringOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SubstringOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("SUBSTRING", SubstringOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, SubstringOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, SubstringOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(SubstringOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
        assertValidArgument(SubstringOperator.INSTANCE, 0, OppModelConstants.XSD_INTEGER_INSTANCE);
        assertValidArgument(SubstringOperator.INSTANCE, 1, OppModelConstants.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SubstringOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(SubstringOperator.INSTANCE, 0, OppModelConstants.XSD_INTEGER_INSTANCE);
    }
}
