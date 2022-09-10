package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTResolvableSchemalessIriStepTest extends ODTTestCase {
    @Test
    void testSchemalessIriInstance() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / ^rdf:type / <booleanPredicate>");
        Assertions.assertEquals(OppModelConstants.getXsdBooleanInstance(), resource);
    }

    @Test
    void testSchemalessIriClass() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / <booleanPredicate>");
        Assertions.assertEquals(OppModelConstants.getXsdBoolean(), resource);
    }
}
