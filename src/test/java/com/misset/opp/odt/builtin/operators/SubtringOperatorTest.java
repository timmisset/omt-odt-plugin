package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtringOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(SubstringOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
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
        assertValidInput(SubstringOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
        assertValidArgument(SubstringOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
        assertValidArgument(SubstringOperator.INSTANCE, 1, OntologyModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(SubstringOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(SubstringOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
    }
}
