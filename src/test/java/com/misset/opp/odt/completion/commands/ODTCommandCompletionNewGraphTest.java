package com.misset.opp.odt.completion.commands;

import com.misset.opp.testCase.OMTCompletionTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionNewGraphTest extends OMTCompletionTestCase {

    @Test
    void testShowsPredicates() {
        String content = insideProcedureRunWithPrefixes(
                "@NEW_GRAPH(<caret>)");
        configureByText(content, true);
        OntClass graph_shape = OppModelConstants.getGraphShape();
        graph_shape.createIndividual("http://test");
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "/<http://test>");
    }

}
