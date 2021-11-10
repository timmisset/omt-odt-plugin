package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.testCase.OntologyTestCase;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTDefineInputParamDelegateTest extends OntologyTestCase {

    @Test
    void testInputParameterWithPrimitive() {
        String content = insideActivityWithPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $param (string)\n" +
                "    */\n" +
                "   DEFINE QUERY <caret>query($param) => $param;");
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            if(elementAtCaret instanceof ODTDefineName) {
                final Set<OntResource> resolve = ((ODTDefineName) elementAtCaret).resolve();
                assertContainsElements(resolve, OppModel.INSTANCE.XSD_STRING_INSTANCE);
            } else {
                Assertions.fail("Could not resolve query");
            }
        });
    }

    @Test
    void testInputParameterWithOntologyClass() {
        String content = insideActivityWithPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $param (ont:ClassA)\n" +
                "    */\n" +
                "   DEFINE QUERY <caret>query($param) => $param;");
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            if(elementAtCaret instanceof ODTDefineName) {
                final Set<OntResource> resolve = ((ODTDefineName) elementAtCaret).resolve();
                assertContainsElements(resolve, OppModel.INSTANCE.getIndividual("http://ontology#ClassA_InstanceA"));
            } else {
                Assertions.fail("Could not resolve query");
            }
        });
    }
}
