package com.misset.opp.odt.completion.commands;

import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionNewGraphTest extends ODTTestCase {

    @Test
    void testShowsPredicates() {
        String content = "@NEW_GRAPH(<caret>)";
        configureByText(content, true);
        OntClass graph_shape = OppModelConstants.getGraphShape();
        graph_shape.createIndividual("http://test");
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/<http://test>");
    }

}
