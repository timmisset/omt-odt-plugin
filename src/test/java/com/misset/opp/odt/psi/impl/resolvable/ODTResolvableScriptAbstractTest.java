package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableScriptAbstractTest extends ODTTestCase {

    @Test
    void testResolvesReturnStatements() {
        String content = "<caret>IF true { RETURN 'a'; } ELSE { RETURN 1; }";
        configureByText(content);
        Set<OntResource> resources = ReadAction.compute(() -> ((ODTScript) myFixture.getFile().getFirstChild()).resolve());
        assertContainsElements(resources, OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdIntegerInstance());
    }

}
