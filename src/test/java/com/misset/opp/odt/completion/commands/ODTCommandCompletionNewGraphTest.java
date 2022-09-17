package com.misset.opp.odt.completion.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionNewGraphTest extends ODTTestCase {

    @Test
    void testShowsPredicates() {
        String content = "@NEW_GRAPH(<caret>)";
        configureByText(content, true);
        OntClass graph_shape = OntologyModelConstants.getGraphShape();
        graph_shape.createIndividual("http://test");
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/<http://test>");
    }

}
