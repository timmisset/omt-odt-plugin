package com.misset.opp.odt.psi.impl.callable;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineStatement;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTDefineStatementTest extends ODTTestCase {

    @Test
    void testSetName() {
        String content = "DEFINE QUERY <caret>query => '';";
        configureByText(content);
        ODTDefineStatement defineStatement = (ODTDefineStatement) ReadAction.compute(myFixture::getElementAtCaret);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            defineStatement.setName("newName");
        });
        String contentAfterRename = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals("DEFINE QUERY newName => '';", contentAfterRename);
    }

    @Test
    void testGetNumberOfArguments() {
        String content = "/**\n" +
                " * @param $paramA (string)\n" +
                " */\n" +
                "DEFINE QUERY <caret>query($paramA) => '';\n";
        configureByText(content);
        ReadAction.run(() -> {
            ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            Assertions.assertEquals(1, defineStatement.minNumberOfArguments());
            Assertions.assertEquals(1, defineStatement.maxNumberOfArguments());
        });
    }

    @Test
    void testGetParamType() {
        String content = "/**\n" +
                " * @param $paramA (string)\n" +
                " */\n" +
                "DEFINE QUERY <caret>query($paramA) => '';\n";
        configureByText(content);
        ReadAction.run(() -> {
            ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            Assertions.assertEquals(Set.of(OppModelConstants.getXsdStringInstance()), defineStatement.getParamType(0));
        });
    }

    @Test
    void testIsUnusedTrue() {
        String content = "DEFINE QUERY <caret>query($paramA) => '';";
        configureByText(content);
        underProgress(() -> ReadAction.run(() -> {
            ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            Assertions.assertTrue(defineStatement.isUnused());
        }));
    }

    @Test
    void testIsUnusedFalse() {
        String content = "DEFINE QUERY <caret>query($paramA) => '';\n" +
                "DEFINE QUERY queryB => query;";
        configureByText(content);
        underProgress(() -> ReadAction.run(() -> {
            ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            Assertions.assertFalse(defineStatement.isUnused());
        }));
    }

    @Test
    void testDelete() {
        String content = "DEFINE QUERY <caret>query($paramA) => '';\n" +
                "DEFINE QUERY queryB => '';";
        configureByText(content);
        ODTDefineStatement defineStatement = (ODTDefineStatement) ReadAction.compute(myFixture::getElementAtCaret);
        WriteCommandAction.runWriteCommandAction(getProject(), defineStatement::delete);
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals("DEFINE QUERY queryB => '';", contentAfterDelete);
    }

    @Test
    void testReturnTypeReturnsTypedInsteadOfResolvable() {
        String content = "/**\n" +
                " * @param $paramA (string)\n" +
                " * @return (integer)" +
                " */\n" +
                "DEFINE QUERY <caret>query($paramA) => '';\n";
        configureByText(content);
        ReadAction.run(() -> {
            ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            Assertions.assertEquals(Set.of(OppModelConstants.getXsdIntegerInstance()), defineStatement.resolve());
        });
    }

    @Test
    void testReturnTypeReturnsUriType() {
        String content = "" +
                "PREFIX ont: <http://ontology#>\n" +
                "/**\n" +
                " * @param $paramA (string)\n" +
                " * @return (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY <caret>query($paramA) => '';\n";
        configureByText(content);
        ReadAction.run(() -> {
            ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            Set<OntResource> resolve = defineStatement.resolve();
            Assertions.assertEquals(OppModel.getInstance().toIndividuals("http://ontology#ClassA"), resolve);
        });
    }
}
