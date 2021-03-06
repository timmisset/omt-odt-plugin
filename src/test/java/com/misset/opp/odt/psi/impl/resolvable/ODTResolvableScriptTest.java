package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTResolvableScriptTest extends OMTOntologyTestCase {

    @Test
    void testResolvesReturnStatements() {
        String content = insideProcedureRunWithPrefixes(
                "<caret>IF true { RETURN 'a'; } ELSE { RETURN 1; }"
        );
        configureByText(content);
        Set<OntResource> resources = ReadAction.compute(() -> ((ODTScript) myFixture.getFile().getFirstChild()).resolve());
        assertContainsElements(resources, OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testDoesntResolvesQueryStatement() {
        String content = insideStandaloneQueryWithPrefixes("query: '<caret>'");
        configureByText(content);
        Set<OntResource> resources = ReadAction.compute(() -> ((ODTScript) myFixture.getFile().getFirstChild()).resolve());
        assertDoesntContain(resources, OppModelConstants.XSD_STRING_INSTANCE);
    }

}
