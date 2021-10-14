package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.inspection.OMTMissingKeysInspection;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OMTOntologyTypeRequiredKeyTest extends InspectionTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(OMTMissingKeysInspection.class);
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
