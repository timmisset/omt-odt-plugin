package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTResolvableSchemalessIriStepAbstractTest extends ODTTestCase {
    @Test
    void testSchemalessIriInstance() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / ^rdf:type / <booleanPredicate>");
        Assertions.assertEquals(OntologyModelConstants.getXsdBooleanInstance(), resource);
    }

    @Test
    void testSchemalessIriClass() {
        final OntResource resource = resolveQueryStatementToSingleResult("/ont:ClassA / <booleanPredicate>");
        Assertions.assertEquals(OntologyModelConstants.getXsdBoolean(), resource);
    }
}
