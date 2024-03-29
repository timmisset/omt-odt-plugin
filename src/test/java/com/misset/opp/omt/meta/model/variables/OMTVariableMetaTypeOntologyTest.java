package com.misset.opp.omt.meta.model.variables;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.resolvable.callable.ODTDefineStatement;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OMTVariableMetaTypeOntologyTest extends OMTTestCase {

    @Test
    void testResolveVariableTypeHasNoType() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       variables:\n" +
                "       -   $variable\n" +
                "       queries: |\n" +
                "           DEFINE QUERY <caret>query => $variable;" +
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
    void testResolveVariableTypeHasPrimitiveType() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       variables:\n" +
                "       -   $variable = 'hello'\n" +
                "       queries: |\n" +
                "           DEFINE QUERY <caret>query => $variable;" +
                "";
        configureByText(content);
        ReadAction.run(() -> {
            final ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            final Set<OntResource> resolve = defineStatement.resolve();
            // no type is provided, should be an empty collection
            Assertions.assertFalse(resolve.isEmpty());
            Assertions.assertEquals(OntologyModelConstants.getXsdStringInstance(), resolve.toArray()[0]);
        });
    }

    @Test
    void testResolveVariableDestructedTypeHasPrimitiveType() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       variables:\n" +
                "       -   name: $variable\n" +
                "           value: 'hello'\n" +
                "       queries: |\n" +
                "           DEFINE QUERY <caret>query => $variable;" +
                "";
        configureByText(content);
        ReadAction.run(() -> {
            final ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            final Set<OntResource> resolve = defineStatement.resolve();
            // no type is provided, should be an empty collection
            Assertions.assertFalse(resolve.isEmpty());
            Assertions.assertEquals(OntologyModelConstants.getXsdStringInstance(), resolve.toArray()[0]);
        });
    }

    @Test
    void testResolveVariableTypeHasOntologyType() {
        String content = withPrefixes("model:\n" +
                "   Activiteit: !Activity\n" +
                "       variables:\n" +
                "       -   $variable = /ont:ClassA / ^rdf:type\n" +
                "       queries: |\n" +
                "           DEFINE QUERY <caret>query => $variable;" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            final ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            final Set<OntResource> resolve = defineStatement.resolve();
            // no type is provided, should be an empty collection
            Assertions.assertFalse(resolve.isEmpty());
            Assertions.assertEquals(createOntologyUri("ClassA"),
                    ((OntResource) resolve.toArray()[0]).asIndividual().getOntClass().getURI());
        });
    }

}
