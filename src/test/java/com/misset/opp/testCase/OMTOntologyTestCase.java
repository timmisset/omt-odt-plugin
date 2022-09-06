package com.misset.opp.testCase;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineStatement;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Set;

public abstract class OMTOntologyTestCase extends OMTTestCase {

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

    protected OntResource createResource(String localName) {
        return createResource("http://ontology#", localName);
    }

    protected Property createProperty(String localName) {
        return oppModel.getProperty(createResource("http://ontology#", localName));
    }

    protected OntClass createClass(String name) {
        return oppModel.getClass(createResource(name));
    }

    protected OntResource createXsdResource(String localName) {
        return createResource("http://www.w3.org/2001/XMLSchema#", localName);
    }

    protected OntResource createResource(String namespace, String localName) {
        return oppModel.getModel().createOntResource(namespace + localName);
    }

    protected OntResource resolveQueryStatementToSingleResult(String query) {
        return resolveQueryStatement(query).stream().findFirst().orElse(null);
    }

    protected Set<OntResource> resolveQueryStatement(String query) {
        // adding <caret> is required to make sure the fixture focus is on the injected ODT fragment
        // otherwise the findElementByText will return null
        String content = insideQueryWithPrefixes("<caret>" + query);
        configureByText(content);
        return ReadAction.compute(() -> myFixture.findElementByText("query", ODTDefineStatement.class).resolve());
    }

    protected Set<OntResource> resolveQueryAtCaret(String content) {
        configureByText(content);
        return ReadAction.compute(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            if (elementAtCaret instanceof ODTDefineStatement) {
                return ((ODTDefineStatement) elementAtCaret).resolve();
            } else {
                Assertions.fail("Could not resolve query");
            }
            return Collections.emptySet();
        });
    }

    protected boolean isIndividualOfClass(Resource resource, Resource classResource) {
        if(resource instanceof Individual) {
            return ((Individual)resource).getOntClass().equals(classResource);
        }
        return false;
    }
}
