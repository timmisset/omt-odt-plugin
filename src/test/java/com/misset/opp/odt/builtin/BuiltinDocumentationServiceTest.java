package com.misset.opp.odt.builtin;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.misset.opp.odt.builtin.commands.AddToCommand;
import com.misset.opp.testCase.ODTTestCase;
import org.commonmark.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class BuiltinDocumentationServiceTest extends ODTTestCase {

    @Test
    void testLoadDocumentationStartup() {
        String content = "Some description";
        WriteCommandAction.runWriteCommandAction(
                getProject(),
                () -> {
                    myFixture.addFileToProject("AddToCommand.md", content);
                }
        );
        final BuiltinDocumentationService documentationService = BuiltinDocumentationService.getInstance(getProject());
        documentationService.getTask().run(null);

        Assertions.assertEquals("<p>Some description</p>\n", AddToCommand.INSTANCE.getDescription(""));
    }

    @Test
    void testShowsErroredFileOnException() {
        try (MockedStatic<Parser> parserMockedStatic = Mockito.mockStatic(Parser.class)) {
            Parser.Builder builder = mock(Parser.Builder.class);
            Parser parser = mock(Parser.class);
            parserMockedStatic.when(Parser::builder).thenReturn(builder);
            doReturn(parser).when(builder).build();
            doThrow(new RuntimeException("some exception")).when(parser).parse(anyString());

            String content = "Some description";
            WriteCommandAction.runWriteCommandAction(
                    getProject(),
                    () -> {
                        myFixture.addFileToProject("MyFile.md", content);
                    }
            );

            final BuiltinDocumentationService documentationService = BuiltinDocumentationService.getInstance(getProject());
            assertThrows(RuntimeException.class,
                    "Error parsing markdown file: /src/MyFile.md, message: some exception",
                    () -> documentationService.getTask().run(null));
        }

    }

    @Test
    void testReturnsNullWhenDocumentNotReadable() {
        try (MockedStatic<FileDocumentManager> documentManagerMockedStatic = Mockito.mockStatic(FileDocumentManager.class)) {
            FileDocumentManager documentManager = mock(FileDocumentManager.class);
            documentManagerMockedStatic.when(FileDocumentManager::getInstance).thenReturn(documentManager);

            doReturn(null).when(documentManager).getDocument(any(VirtualFile.class));

            String content = "Some description";
            WriteCommandAction.runWriteCommandAction(
                    getProject(),
                    () -> {
                        myFixture.addFileToProject("AddToCommand.md", content);
                    }
            );
            final BuiltinDocumentationService documentationService = BuiltinDocumentationService.getInstance(getProject());
            documentationService.getTask().run(null);

            Assertions.assertNull(AddToCommand.INSTANCE.getDescription(""));
        }

    }

}
