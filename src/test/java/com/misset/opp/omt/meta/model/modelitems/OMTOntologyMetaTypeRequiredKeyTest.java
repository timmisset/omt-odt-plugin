package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTOntologyMetaTypeRequiredKeyTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTMissingKeysInspection.class));
    }

    @Test
    void testOntologyTypeRequired() {
        String content = "model:\n" +
                "   Ontology: !Ontology\n" +
                "       prefix: test\n";
        configureByText(content);
        inspection.assertHasError("Missing required key(s): 'classes'");
    }

    @Test
    void testOntologyClassTypeRequired() {
        String content = "model:\n" +
                "   Ontology: !Ontology\n" +
                "       prefix: test\n" +
                "       classes: \n" +
                "       - id: test\n";
        configureByText(content);
        inspection.assertHasError("Missing required key(s): 'properties'");
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
        inspection.assertHasError("Missing required key(s): 'type'");
    }
}
