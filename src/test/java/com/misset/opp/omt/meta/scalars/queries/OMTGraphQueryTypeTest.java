package com.misset.opp.omt.meta.scalars.queries;

import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTGraphQueryTypeTest extends OMTTestCase {
    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
    }

    @Test
    void testHasErrorWhenNotResolvingToGraph() {
        configureByText(insideActivityWithPrefixes("graphs:\n" +
                "   live:\n" +
                "   - /ont:ClassA"));
        inspection.assertHasError("Expected a query that resolves to a graph");
    }

    @Test
    void testNoErrorWhenResolvingToGraph() {
        configureByText(insideActivityWithPrefixes("graphs:\n" +
                "   live:\n" +
                "   - $medewerkerGraph"));
        inspection.assertNoError("Expected a query that resolves to a graph");
    }
}
