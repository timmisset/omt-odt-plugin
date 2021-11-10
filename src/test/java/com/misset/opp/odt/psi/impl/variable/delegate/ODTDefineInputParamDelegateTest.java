package com.misset.opp.odt.psi.impl.variable.delegate;

import com.misset.opp.testCase.OntologyTestCase;
import com.misset.opp.ttl.OppModel;
import org.junit.jupiter.api.Test;

class ODTDefineInputParamDelegateTest extends OntologyTestCase {

    @Test
    void testInputParameterWithPrimitive() {
        String content = insideActivityWithPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $param (string)\n" +
                "    */\n" +
                "   DEFINE QUERY <caret>query($param) => $param;");
        assertContainsElements(resolveQueryAtCaret(content),
                OppModel.INSTANCE.XSD_STRING_INSTANCE);
    }

    @Test
    void testInputParameterWithOntologyClass() {
        String content = insideActivityWithPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $param (ont:ClassA)\n" +
                "    */\n" +
                "   DEFINE QUERY <caret>query($param) => $param;");
        assertContainsElements(resolveQueryAtCaret(content),
                OppModel.INSTANCE.getIndividual("http://ontology#ClassA_InstanceA"));
    }
}
