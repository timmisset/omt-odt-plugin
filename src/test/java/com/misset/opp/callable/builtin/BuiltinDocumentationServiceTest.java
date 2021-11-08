package com.misset.opp.callable.builtin;

import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.callable.builtin.commands.AddToCommand;
import com.misset.opp.testCase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuiltinDocumentationServiceTest extends ODTTestCase {

    @Test
    void testLoadDocumentationStartup() {
        String content = "Some description";
        WriteCommandAction.runWriteCommandAction(
                getProject(),
                () -> { myFixture.addFileToProject("AddToCommand.md", content); }
        );
        final BuiltinDocumentationService documentationService = BuiltinDocumentationService.getInstance(getProject());
        documentationService.getTask().run(null);

        Assertions.assertEquals("<p>Some description</p>\n", AddToCommand.INSTANCE.getDescription(""));
    }


}
