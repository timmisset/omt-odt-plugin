package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTOntologyMetaTypeRequiredKeyTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTMissingKeysInspection.class);
    }

    @Test
    void testOntologyTypeRequired() {
        String content = "model:\n" +
                "   Ontology: !Ontology\n" +
                "       prefix: test\n";
        configureByText(content);
        assertHasError("Missing required key(s): 'classes'");
    }

    @Test
    void testOntologyClassTypeRequired() {
        String content = "model:\n" +
                "   Ontology: !Ontology\n" +
                "       prefix: test\n" +
                "       classes: \n" +
                "       - id: test\n";
        configureByText(content);
        assertHasError("Missing required key(s): 'properties'");
    }

    @Test
    void testOntologyPropertyTypeRequired() {
        String content = "model:\n" +
                "   Ontology: !Ontology\n" +
                "       prefix: test\n" +
                "       classes: \n" +
                "       -   id: test\n" +
                "           properties:\n" +
                "               propA:\n" +
                "                   multiple: false";
        configureByText(content);
        assertHasError("Missing required key(s): 'type'");
    }
}
