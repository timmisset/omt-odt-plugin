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

class ClearGraphCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(ClearGraphCommand.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testArgumentTypes() {
        testArgument(ClearGraphCommand.INSTANCE,
                0,
                OppModel.INSTANCE.MEDEWERKER_GRAPH,
                TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testName() {
        Assertions.assertEquals("CLEAR_GRAPH", ClearGraphCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(ClearGraphCommand.INSTANCE.isVoid());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsNamedGraphForIndex0() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = ClearGraphCommand.INSTANCE.getAcceptableArgumentTypeWithContext(0, call);
        Assertions.assertEquals(1, acceptableArgumentTypeWithContext.size());
        Assertions.assertEquals(OppModel.INSTANCE.NAMED_GRAPH, acceptableArgumentTypeWithContext.iterator().next());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsNullForIndexOtherThan0() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = ClearGraphCommand.INSTANCE.getAcceptableArgumentTypeWithContext(1, call);
        Assertions.assertNull(acceptableArgumentTypeWithContext);
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, ClearGraphCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, ClearGraphCommand.INSTANCE.maxNumberOfArguments());
    }
}
