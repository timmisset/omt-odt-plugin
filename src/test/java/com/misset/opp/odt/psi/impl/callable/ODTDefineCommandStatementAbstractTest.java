package com.misset.opp.odt.psi.impl.callable;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.psi.resolvable.callable.ODTDefineStatement;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTDefineCommandStatementAbstractTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void testResolveCommandStatement() {
        String content = "DEFINE COMMAND <caret>command => { RETURN 'test'; }";
        configureByText(content);
        ODTDefineStatement defineStatement = (ODTDefineStatement) ReadAction.compute(myFixture::getElementAtCaret);
        Set<OntResource> resolve = ReadAction.compute(defineStatement::resolve);
        assertContainsElements(resolve, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testResolveCommandStatementMultiType() {
        String content = "DEFINE COMMAND <caret>command => { " +
                "IF x { RETURN 'test'; } ELSE { RETURN 1; } }";
        configureByText(content);
        ODTDefineStatement defineStatement = (ODTDefineStatement) ReadAction.compute(myFixture::getElementAtCaret);
        Set<OntResource> resolve = ReadAction.compute(defineStatement::resolve);
        assertContainsElements(resolve, OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdIntegerInstance());
    }

}
