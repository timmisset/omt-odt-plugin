package com.misset.opp.odt.psi.impl.callable;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.testCase.ODTTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTBaseDefineCommandStatementTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        OMTOntologyTestCase.initOntologyModel();
    }

    @Test
    void testResolveCommandStatement() {
        String content = "DEFINE COMMAND <caret>command => { RETURN 'test'; }";
        configureByText(content);
        ODTDefineStatement defineStatement = (ODTDefineStatement) ReadAction.compute(myFixture::getElementAtCaret);
        Set<OntResource> resolve = ReadAction.compute(defineStatement::resolve);
        assertContainsElements(resolve, OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Test
    void testResolveCommandStatementMultiType() {
        String content = "DEFINE COMMAND <caret>command => { " +
                "IF x { RETURN 'test'; } ELSE { RETURN 1; } }";
        configureByText(content);
        ODTDefineStatement defineStatement = (ODTDefineStatement) ReadAction.compute(myFixture::getElementAtCaret);
        Set<OntResource> resolve = ReadAction.compute(defineStatement::resolve);
        assertContainsElements(resolve, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE);
    }

}
