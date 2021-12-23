package com.misset.opp.omt.meta.scalars.queries;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTGraphQueryTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTValueInspection.class);
    }

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        OMTOntologyTestCase.initOntologyModel();
    }

    @Test
    void testHasErrorWhenNotResolvingToGraph() {
        configureByText(insideActivityWithPrefixes("graphs:\n" +
                "   live:\n" +
                "   - /ont:ClassA"));
        assertHasError("Expected a query that resolves to a graph");
    }

    @Test
    void testNoErrorWhenResolvingToGraph() {
        configureByText(insideActivityWithPrefixes("graphs:\n" +
                "   live:\n" +
                "   - $medewerkerGraph"));
        assertNoError("Expected a query that resolves to a graph");
    }
}