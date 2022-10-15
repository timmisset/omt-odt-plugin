package com.misset.opp.odt.completion;

import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTEquationStatementCompletionTest extends ODTTestCase {

    @Test
    void testShowsClassesForRdfType() {
        String content = withPrefixes("/ont:ClassB / ^rdf:type / rdf:type == <caret>");
        configureByText(content, true);
        assertContainsElements(completion.getLookupStrings(), "/ont:ClassB", "/ont:ClassBSub");
    }

    @Test
    void testDoesNotShowClassesForInstances() {
        String content = withPrefixes("/ont:ClassB / ^rdf:type == <caret>");
        configureByText(content, true);
        assertDoesntContain(completion.getLookupStrings(), "/ont:ClassB", "/ont:ClassBSub");
    }

    @Test
    void testShowsInstancesForInstanceOfClass() {
        OntClass ontClass = OntologyModel.getInstance(getProject()).getClass("http://ontology#ClassB");
        ontologyModel.createIndividual(ontClass, "http://ontology#ClassB_1");
        ontologyModel.createIndividual(ontClass, "http://ontology#ClassB_2");
        String content = withPrefixes("/ont:ClassB / ^rdf:type == <caret>");
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/ont:ClassB_1", "/ont:ClassB_2");
        assertDoesntContain(lookupStrings, "/ont:ClassB_INSTANCE");
    }

}
