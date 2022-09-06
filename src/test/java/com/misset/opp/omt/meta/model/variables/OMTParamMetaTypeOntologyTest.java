package com.misset.opp.omt.meta.model.variables;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineStatement;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OMTParamMetaTypeOntologyTest extends OMTOntologyTestCase {

    @Test
    void testResolveParameterTypeHasNoType() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param\n" +
                "       queries: |\n" +
                "           DEFINE QUERY <caret>query => $param;" +
                "";
        configureByText(content);
        ReadAction.run(() -> {
            final ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            final Set<OntResource> resolve = defineStatement.resolve();
            // no type is provided, should be an empty collection
            Assertions.assertTrue(resolve.isEmpty());
        });

    }

    @Test
    void testResolveParameterTypeHasPrimitiveType() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param (string)\n" +
                "       queries: |\n" +
                "           DEFINE QUERY <caret>query => $param;" +
                "";
        configureByText(content);
        ReadAction.run(() -> {
            final ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            final Set<OntResource> resolve = defineStatement.resolve();
            Assertions.assertFalse(resolve.isEmpty());
            Assertions.assertEquals(OppModelConstants.getXsdStringInstance(), resolve.toArray()[0]);
        });
    }

    @Test
    void testResolveParameterTypeDestructedHasPrimitiveType() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: $param\n" +
                "           type: string\n" +
                "       queries: |\n" +
                "           DEFINE QUERY <caret>query => $param;" +
                "";
        configureByText(content);
        ReadAction.run(() -> {
            final ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            final Set<OntResource> resolve = defineStatement.resolve();
            Assertions.assertFalse(resolve.isEmpty());
            Assertions.assertEquals(OppModelConstants.getXsdStringInstance(), resolve.toArray()[0]);
        });
    }

    @Test
    void testResolveParameterTypeHasOntologyType() {
        String content = withPrefixes("model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param (ont:ClassA)\n" +
                "       queries: |\n" +
                "           DEFINE QUERY <caret>query => $param;" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            final ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            final Set<OntResource> resolve = defineStatement.resolve();
            Assertions.assertFalse(resolve.isEmpty());
            Assertions.assertEquals(createClass("ClassA"), ((OntResource) resolve.toArray()[0]).asIndividual().getOntClass());
        });
    }

}
