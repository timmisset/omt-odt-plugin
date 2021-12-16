package com.misset.opp.ttl.psi;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.testCase.TTLTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TTLFileImplTest extends TTLTestCase {

    TTLFile ttlFile;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ttlFile = configureByText("@prefix ont: <http://ontology#> .\n" +
                "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
                "@prefix sh: <http://www.w3.org/ns/shacl#> .\n" +
                "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
                "\n" +
                "<http://ontology/ontologyA>\n" +
                "  a owl:Ontology ;\n" +
                "  owl:versionInfo \"Created manually\" ;\n" +
                ".\n" +
                "ont:ClassA\n" +
                "  a owl:Class ;\n" +
                "  a sh:NodeShape ;\n" +
                "  sh:property [\n" +
                "      a sh:PropertyShape ;\n" +
                "      sh:path ont:booleanPredicate ;\n" +
                "      sh:datatype xsd:boolean ;\n" +
                "    ] ;\n" +
                "  sh:property [\n" +
                "      a sh:PropertyShape ;\n" +
                "      sh:path ont:classPredicate ;\n" +
                "      sh:class ont:ClassBSub ;\n" +
                "      sh:minCount 1;\n" +
                "      sh:maxCount 1;\n" +
                "    ] ;\n" +
                ".\n" +
                "ont:ClassA_InstanceA\n" +
                "  a ont:ClassA ;\n" +
                "  ont:booleanPredicate \"true\"^^xsd:boolean ;\n" +
                ".\n");
    }

    @Test
    void testGetSubjectFromTTLFileFromIRI() {
        ReadAction.run(() -> {
            TTLSubject subject = ttlFile.getSubject("http://ontology/ontologyA");
            Assertions.assertNotNull(subject);
        });
    }

    @Test
    void testGetSubjectFromTTLFileFromCurie() {
        ReadAction.run(() -> {
            TTLSubject subject = ttlFile.getSubject("http://ontology#ClassA");
            Assertions.assertNotNull(subject);
        });
    }

    @Test
    void testGetPredicateFromTTLFile() {
        ReadAction.run(() -> {
            TTLObject object = ttlFile.getPredicate("http://ontology#ClassA", "http://ontology#booleanPredicate");
            Assertions.assertNotNull(object);
        });
    }

    @Test
    void testGetObjectFromTTLFile() {
        ReadAction.run(() -> {
            TTLObject object = ttlFile.getObject("http://ontology#ClassA", "http://ontology#classPredicate");
            Assertions.assertNotNull(object);
            assertEquals("http://ontology#ClassBSub", object.getIri().getQualifiedUri());
        });
    }
}