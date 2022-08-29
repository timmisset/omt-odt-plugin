package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTResolvableSchemalessIriStepTest extends OMTOntologyTestCase {
    @Test
    void testSchemalessIriInstance() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / ^rdf:type / <booleanPredicate>");
        Assertions.assertEquals(OppModelConstants.XSD_BOOLEAN_INSTANCE, resource);
    }

    @Test
    void testSchemalessIriClass() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / <booleanPredicate>");
        Assertions.assertEquals(OppModelConstants.XSD_BOOLEAN, resource);
    }
}
