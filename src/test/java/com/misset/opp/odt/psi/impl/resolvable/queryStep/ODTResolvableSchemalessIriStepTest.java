package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.misset.opp.testCase.OntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTResolvableSchemalessIriStepTest extends OntologyTestCase {
    @Test
    void testSchemalessIriInstance() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / ^rdf:type / <booleanPredicate>");
        Assertions.assertEquals(oppModel.XSD_BOOLEAN_INSTANCE, resource);
    }

    @Test
    void testSchemalessIriClass() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / <booleanPredicate>");
        Assertions.assertEquals(oppModel.XSD_BOOLEAN, resource);
    }
}
