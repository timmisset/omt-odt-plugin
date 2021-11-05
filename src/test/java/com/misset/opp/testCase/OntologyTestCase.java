package com.misset.opp.testCase;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.OppModelLoader;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class OntologyTestCase extends OMTTestCase {

    protected OppModel oppModel;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        setOntologyModel();
    }

    @Override
    @AfterEach
    protected void tearDown() {
        super.tearDown();
    }

    protected void setOntologyModel() {
        oppModel = new OppModelLoader().read(getRootPath());
    }


    private File getRootPath() {
        return getRootPath("model", "root.ttl");
    }
    private File getRootPath(String folder, String file) {
        Path resourceDirectory = Paths.get("src","test","resources", folder, file);
        return resourceDirectory.toFile();
    }

    protected OntResource createResource(String localName) {
        return createResource("http://ontology#", localName);
    }
    protected Property createProperty(String localName) {
        return oppModel.createProperty(createResource("http://ontology#", localName));
    }
    protected OntClass createClass(String name) {
        return oppModel.createClass(createResource(name));
    }
    protected Individual createIndividual(String className) {
        return oppModel.createIndividual(createResource(className));
    }
    protected OntResource createXsdResource(String localName) {
        return createResource("http://www.w3.org/2001/XMLSchema#", localName);
    }
    protected OntResource createResource(String namespace, String localName) {
        return oppModel.createResource(namespace + localName);
    }

    protected Set<OntResource> resolveQueryStatement(String query) {
        // adding <caret> is required to make sure the fixture focus is on the injected ODT fragment
        // otherwise the findElementByText will return null
        String content = insideQueryWithPrefixes("<caret>" + query);
        configureByText(content);
        return ReadAction.compute(() -> myFixture.findElementByText("query", ODTDefineName.class).resolve());
    }
    protected boolean isIndividualOfClass(Resource resource, Resource classResource) {
        if(resource instanceof Individual) {
            return ((Individual)resource).getOntClass().equals(classResource);
        }
        return false;
    }


    protected void assertStatementContains(Set<Statement> statements, Property predicate, RDFNode object) {
        Assertions.assertTrue(statements.stream().anyMatch(
                statement -> statement.getPredicate().equals(predicate) && statement.getObject().equals(object)
        ));
    }
    protected void assertStatementNotContains(Set<Statement> statements, Property predicate, RDFNode object) {
        Assertions.assertTrue(statements.stream().noneMatch(
                statement -> statement.getPredicate().equals(predicate) && statement.getObject().equals(object)
        ));
    }
}
