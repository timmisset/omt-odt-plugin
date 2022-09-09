package com.misset.opp.odt.completion;

import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.ttl.model.OppModel;
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
        OntClass ontClass = OppModel.getInstance().getClass("http://ontology#ClassB");
        oppModel.createIndividual(ontClass, "http://ontology#ClassB_1");
        oppModel.createIndividual(ontClass, "http://ontology#ClassB_2");
        String content = withPrefixes("/ont:ClassB / ^rdf:type == <caret>");
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/ont:ClassB_1", "/ont:ClassB_2");
        assertDoesntContain(lookupStrings, "/ont:ClassB_INSTANCE");
    }

}
