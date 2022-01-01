package com.misset.opp.odt.psi.impl.callable;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.testCase.ODTTestCase;
import com.misset.opp.ttl.OppModel;
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
    void testGetDescription() {
        String content = "/**\n" +
                " * @param $paramA (string)\n" +
                " */\n" +
                "DEFINE QUERY <caret>query($paramA) => '';\n";
        configureByText(content);
        ReadAction.run(() -> {
            ODTDefineStatement defineStatement = (ODTDefineStatement) myFixture.getElementAtCaret();
            String description = defineStatement.getDescription(null);
            Assertions.assertEquals("query<br>" +
                    "type: DEFINE QUERY<br>" +
                    "params:<br>" +
                    "- $paramA (string)<br>", description);
        });
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
            Assertions.assertEquals(Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE), defineStatement.getParamType(0));
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

}
