package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTDefineInputParamDelegateTest extends OMTOntologyTestCase {

    @Test
    void testInputParameterWithPrimitive() {
        String content = insideActivityWithPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $param (string)\n" +
                "    */\n" +
                "   DEFINE QUERY <caret>query($param) => $param;");
        assertContainsElements(resolveQueryAtCaret(content),
                OppModelConstants.XSD_STRING_INSTANCE);
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

    @Test
    void testDeleteRemovesParameter() {
        String content = insideActivityWithPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $param (ont:ClassA)\n" +
                "    */\n" +
                "   DEFINE QUERY query($<caret>param) => '';");
        OMTFile omtFile = configureByText(content);
        ODTVariable parameter = (ODTVariable) ReadAction.compute(() -> myFixture.getElementAtCaret());
        WriteCommandAction.runWriteCommandAction(getProject(), parameter::delete);
        String contentAfterDelete = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("" +
                "queries: |\n" +
                "  DEFINE QUERY query => '';"), contentAfterDelete);
    }

    @Test
    void testDeleteRemovesSingleParameter() {
        String content = insideActivityWithPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $paramA (ont:ClassA)\n" +
                "    * @param $paramB (ont:ClassA)\n" +
                "    */\n" +
                "   DEFINE QUERY query($<caret>paramA, $paramB) => '';");
        OMTFile omtFile = configureByText(content);
        ODTVariable parameter = (ODTVariable) ReadAction.compute(() -> myFixture.getElementAtCaret());
        WriteCommandAction.runWriteCommandAction(getProject(), parameter::delete);
        String contentAfterDelete = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("" +
                "queries: |\n" +
                "  /**\n" +
                "   * @param $paramB (ont:ClassA)\n" +
                "   */\n" +
                "  DEFINE QUERY query($paramB) => '';"), contentAfterDelete);
    }

    @Test
    void testDeleteRemovesParameterAndCallArgument() {
        String content = insideActivityWithPrefixes("" +
                "queries: |\n" +
                "   DEFINE QUERY query($<caret>param) => '';\n" +
                "   DEFINE QUERY anotherQuery => query('test');");
        OMTFile omtFile = configureByText(content);
        ODTVariable parameter = (ODTVariable) ReadAction.compute(() -> myFixture.getElementAtCaret());
        WriteCommandAction.runWriteCommandAction(getProject(), parameter::delete);
        String contentAfterDelete = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("" +
                "queries: |\n" +
                "  DEFINE QUERY query => '';\n" +
                "  DEFINE QUERY anotherQuery => query;"), contentAfterDelete);
    }
}
