package com.misset.opp.ttl;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class OppModelLoaderTest {

    @Test
    void testLoadModel() {
        final OntModel ontModel = getOntologyModel();
        final List<Resource> resources = ontModel.listSubjects().toList();
        Assertions.assertEquals(2, ontModel.listSubModels().toList().size());
        Assertions.assertTrue(resources.contains(ontModel.createResource("http://ontology#ClassA")));
        Assertions.assertTrue(resources.contains(ontModel.createResource("http://ontology#ClassB")));
        Assertions.assertFalse(resources.contains(ontModel.createResource("http://ontology#ClassC")));
        Assertions.assertTrue(resources.contains(ontModel.createResource("http://ontology#ClassD")));
    }

    @Test
    void testLoadModelWithRecursion() {
        final OntModel ontModel = new OppModelLoader()
                .read(getRootPath("model-with-recursion", "root.ttl"))
                .getShaclModel();
        Assertions.assertEquals(0, ontModel.listSubModels().toList().size());
    }

    private OntModel getOntologyModel() {
        return new OppModelLoader().read(getRootPath()).getShaclModel();
    }

    private File getRootPath() {
        return getRootPath("model", "root.ttl");
    }
    private File getRootPath(String folder, String file) {
        Path resourceDirectory = Paths.get("src","test","resources", folder, file);
        return resourceDirectory.toFile();
    }
}
