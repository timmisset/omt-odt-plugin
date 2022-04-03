package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.mock;

class CopyInGraphCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(CopyInGraphCommand.INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(CopyInGraphCommand.INSTANCE,
                0,
                OppModel.INSTANCE.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_INSTANCES,
                OppModel.INSTANCE.XSD_STRING);

        testArgument(CopyInGraphCommand.INSTANCE,
                1,
                OppModel.INSTANCE.MEDEWERKER_GRAPH,
                TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);

        testArgument(CopyInGraphCommand.INSTANCE,
                2,
                OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testName() {
        Assertions.assertEquals("COPY_IN_GRAPH", CopyInGraphCommand.INSTANCE.getName());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsNamedGraphForIndex1() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = CopyInGraphCommand.INSTANCE.getAcceptableArgumentTypeWithContext(1, call);
        Assertions.assertEquals(1, acceptableArgumentTypeWithContext.size());
        Assertions.assertEquals(OppModel.INSTANCE.NAMED_GRAPH, acceptableArgumentTypeWithContext.iterator().next());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsBooleanForIndex2() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = CopyInGraphCommand.INSTANCE.getAcceptableArgumentTypeWithContext(2, call);
        Assertions.assertEquals(1, acceptableArgumentTypeWithContext.size());
        Assertions.assertEquals(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE, acceptableArgumentTypeWithContext.iterator().next());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsNullForIndexOtherThan1Or2() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = CopyInGraphCommand.INSTANCE.getAcceptableArgumentTypeWithContext(0, call);
        Assertions.assertNull(acceptableArgumentTypeWithContext);
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, CopyInGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, CopyInGraphCommand.INSTANCE.maxNumberOfArguments());
    }
}
