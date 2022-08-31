package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTShapeQueryTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTValueInspection.class);
    }

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        OMTOntologyTestCase.initOntologyModel();
        OppModel.getInstance().createIndividual(OppModelConstants.getGraphShape(), "http://shapeA");
    }

    @Test
    void testHasErrorWhenNotResolvingToGraph() {
        String content = withPrefixes("model:\n" +
                "   GraphShapeHandler: !GraphShapeHandlers\n" +
                "       id: myId\n" +
                "       shape: /ont:ClassA\n" +
                "\n");
        configureByText(content);
        assertHasError("Expected a query that resolves to a GraphShape");
    }

    @Test
    void testNoErrorWhenResolvingToGraph() {
        String content = "model:\n" +
                "   GraphShapeHandler: !GraphShapeHandlers\n" +
                "       id: myId\n" +
                "       shape: /<http://shapeA>\n" +
                "\n";
        configureByText(content);
        assertNoError("Expected a query that resolves to a GraphShape");
    }
}
