package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTDefineInputParamDelegateTest extends ODTTestCase {

    @Test
    void testInputParameterWithPrimitive() {
        String content = "/**\n" +
                " * @param $param (string)\n" +
                " */\n" +
                "DEFINE QUERY <caret>query($param) => $param;";
        assertContainsElements(resolveQueryAtCaret(content),
                OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testInputParameterWithOntologyClass() {
        String content = "/**\n" +
                " * @param $param (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY <caret>query($param) => $param;";
        assertContainsElements(resolveQueryAtCaret(withPrefixes(content)),
                OntologyModel.getInstance(getProject()).getIndividual("http://ontology#ClassA_InstanceA"));
    }

    @Test
    void testDeleteRemovesParameter() {
        String content = "/**\n" +
                " * @param $param (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query($<caret>param) => '';";
        ODTFile omtFile = configureByText(content);
        ODTVariable parameter = (ODTVariable) ReadAction.compute(() -> myFixture.getElementAtCaret());
        WriteCommandAction.runWriteCommandAction(getProject(), parameter::delete);
        String contentAfterDelete = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals("DEFINE QUERY query => '';", contentAfterDelete);
    }

    @Test
    void testDeleteRemovesSingleParameter() {
        String content = "/**\n" +
                " * @param $paramA (ont:ClassA)\n" +
                " * @param $paramB (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query($<caret>paramA, $paramB) => '';";
        ODTFile omtFile = configureByText(content);
        ODTVariable parameter = (ODTVariable) ReadAction.compute(() -> myFixture.getElementAtCaret());
        WriteCommandAction.runWriteCommandAction(getProject(), parameter::delete);
        String contentAfterDelete = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals("/**\n" +
                " * @param $paramB (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query($paramB) => '';", contentAfterDelete);
    }

    @Test
    void testDeleteRemovesParameterAndCallArgument() {
        String content = "DEFINE QUERY query($<caret>param) => '';\n" +
                "DEFINE QUERY anotherQuery => query('test');";
        ODTFile omtFile = configureByText(content);
        ODTVariable parameter = (ODTVariable) ReadAction.compute(() -> myFixture.getElementAtCaret());
        WriteCommandAction.runWriteCommandAction(getProject(), parameter::delete);
        String contentAfterDelete = ReadAction.compute(omtFile::getText);
        Assertions.assertEquals("DEFINE QUERY query => '';\n" +
                "DEFINE QUERY anotherQuery => query;", contentAfterDelete);
    }
}
