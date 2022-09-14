package com.misset.opp.omt.meta.scalars.queries;

import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTShapeQueryTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
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
        inspection.assertHasError("Expected a query that resolves to a GraphShape");
    }

    @Test
    void testNoErrorWhenResolvingToGraph() {
        String content = "model:\n" +
                "   GraphShapeHandler: !GraphShapeHandlers\n" +
                "       id: myId\n" +
                "       shape: /<http://shapeA>\n" +
                "\n";
        configureByText(content);
        inspection.assertNoError("Expected a query that resolves to a GraphShape");
    }
}
