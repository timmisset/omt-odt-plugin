package com.misset.opp.model;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Resource;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class OntologyModelLoaderTest extends LightJavaCodeInsightFixtureTestCase {

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void testLoadModel() {
        final OntModel ontModel = getOntologyModel();
        final List<Resource> resources = ontModel.listSubjects().toList();
        Assertions.assertTrue(resources.contains(ontModel.createResource("http://ontology#ClassA")));
        Assertions.assertTrue(resources.contains(ontModel.createResource("http://ontology#ClassB")));
        Assertions.assertTrue(resources.contains(ontModel.createResource("http://ontology#ClassC")));
        Assertions.assertFalse(resources.contains(ontModel.createResource("http://ontology#ClassNotIncluded")));
        Assertions.assertTrue(resources.contains(ontModel.createResource("http://ontology#ClassD")));
    }

    @Test
    void testLoadModelWithRecursion() {
        OntologyModelLoader.getInstance(getProject())
                .read(getRootPath("model-with-recursion", "root.ttl"));
        OntModel ontModel = OntologyModelTranslator.getInstance(getProject()).getShaclModel();
        Assertions.assertEquals(1, ontModel.listSubModels().toList().size());
    }

    @Test
    @Ignore("Add a full ontology to the test folder before enabling this test, not included in the repo")
    void testLoadFullOntology() {
        Assertions.assertDoesNotThrow(() -> {
            OntologyModelLoader.getInstance(getProject())
                    .read(getRootPath("full-opp-model", "root.ttl"));
        });
    }

    private OntModel getOntologyModel() {
        OntologyModelLoader.getInstance(getProject()).read(getRootPath());
        return OntologyModelTranslator.getInstance(getProject()).getShaclModel();
    }

    private File getRootPath() {
        return getRootPath("model", "root.ttl");
    }
    private File getRootPath(String folder, String file) {
        Path resourceDirectory = Paths.get("src","test","resources", folder, file);
        return resourceDirectory.toFile();
    }
}
