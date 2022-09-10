package com.misset.opp.odt.inspection.calls.commands;

import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.CallableType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTCommandInspectionLoadOntologyTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCommandInspectionLoadOntology.class));
    }

    @Test
    void testLoadOntologyThrowsErrorWhenNotReferencingAnOntology() {
        String content = " @LOAD_ONTOLOGY(MyProcedure);";
        ODTFileTestImpl odtFileTest = configureByText(content);

        Callable callable = mock(Callable.class);
        doReturn("MyProcedure").when(callable).getCallId();
        doReturn(CallableType.UNKNOWN).when(callable).getType();
        odtFileTest.addCallable(callable);

        inspection.assertHasError(ODTCommandInspectionLoadOntology.REQUIRES_A_REFERENCE_TO_AN_ONTOLOGY);
    }

    @Test
    void testLoadOntologyThrowsNoErrorWhenReferencingAnOntology() {
        String content = "@LOAD_ONTOLOGY(MyOntology);";
        ODTFileTestImpl odtFileTest = configureByText(content);

        Callable callable = mock(Callable.class);
        doReturn("MyOntology").when(callable).getCallId();
        doReturn(CallableType.ONTOLOGY).when(callable).getType();
        odtFileTest.addCallable(callable);

        inspection.assertNoError(ODTCommandInspectionLoadOntology.REQUIRES_A_REFERENCE_TO_AN_ONTOLOGY);
    }
}
