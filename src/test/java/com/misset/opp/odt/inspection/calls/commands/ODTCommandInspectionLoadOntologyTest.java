package com.misset.opp.odt.inspection.calls.commands;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTCommandInspectionLoadOntologyTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCommandInspectionLoadOntology.class);
    }

    @Test
    void testLoadOntologyThrowsErrorWhenNotReferencingAnOntology() {
        String content = "model:\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           @LOAD_ONTOLOGY(MyProcedure);\n" +
                "\n" +
                "   MyProcedure: !Procedure\n" +
                "       onRun: \n" +
                "           @LOG('do something');";
        configureByText(content);
        assertHasError(ODTCommandInspectionLoadOntology.REQUIRES_A_REFERENCE_TO_AN_ONTOLOGY);
    }

    @Test
    void testLoadOntologyThrowsNoErrorWhenReferencingAnOntology() {
        String content = "model:\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           @LOAD_ONTOLOGY(MyOntology);\n" +
                "\n" +
                "   MyOntology: !Ontology\n" +
                "       prefix: abc\n" +
                "";
        configureByText(content);
        assertNoError(ODTCommandInspectionLoadOntology.REQUIRES_A_REFERENCE_TO_AN_ONTOLOGY);
    }
}
