package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CopyInGraphCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(CopyInGraphCommand.INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(CopyInGraphCommand.INSTANCE,
                0,
                OntologyModelConstants.getXsdStringInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_INSTANCES,
                OntologyModelConstants.getXsdString());

        testArgument(CopyInGraphCommand.INSTANCE,
                1,
                OntologyModelConstants.getMedewerkerGraph(),
                OntologyValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);

        testArgument(CopyInGraphCommand.INSTANCE,
                2,
                OntologyModelConstants.getXsdBooleanInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("COPY_IN_GRAPH", CopyInGraphCommand.INSTANCE.getName());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(CopyInGraphCommand.INSTANCE, 1, OntologyModelConstants.getNamedGraph());
        assertGetAcceptableArgumentType(CopyInGraphCommand.INSTANCE, 2, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(CopyInGraphCommand.INSTANCE, 3);
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, CopyInGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, CopyInGraphCommand.INSTANCE.maxNumberOfArguments());
    }
}
