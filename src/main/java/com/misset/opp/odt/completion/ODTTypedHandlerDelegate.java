package com.misset.opp.odt.completion;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTTypes;
import org.jetbrains.annotations.NotNull;

public class ODTTypedHandlerDelegate extends TypedHandlerDelegate {

    private static final TokenSet ROOT_INDICATOR = TokenSet.create(
            ODTTypes.COMMA, ODTTypes.EQUALS, ODTTypes.LAMBDA, ODTTypes.PARENTHESES_OPEN, ODTTypes.BRACKET_OPEN, ODTTypes.CURLY_OPEN
    );

    @Override
    public @NotNull Result checkAutoPopup(char charTyped,
                                          @NotNull Project project,
                                          @NotNull Editor editor,
                                          @NotNull PsiFile file) {
        if (file instanceof ODTFile && charTyped == '/') {
            AutoPopupController.getInstance(project).scheduleAutoPopup(editor, CompletionType.BASIC, f -> true);
            return Result.STOP;
        }
        return Result.CONTINUE;
    }

    @Override
    public @NotNull Result beforeCharTyped(char c,
                                           @NotNull Project project,
                                           @NotNull Editor editor,
                                           @NotNull PsiFile file,
                                           @NotNull FileType fileType) {
        if (file instanceof ODTFile && c == '/') {
            Document document = editor.getDocument();
            if (!document.isWritable()) {
                return Result.CONTINUE;
            }
            int offset = editor.getCaretModel().getOffset();
            PsiElement previousElement = getFirstNonWhitespace(file, offset - 1);
            if (previousElement == null || ROOT_INDICATOR.contains(previousElement.getNode().getElementType())) {
                // escape when there should be no additional space entered
                return Result.CONTINUE;
            } else {
                document.insertString(offset, "/ ");
                editor.getCaretModel().moveCaretRelatively(2, 0, false, false, false);
                return Result.STOP;
            }
        }
        return Result.CONTINUE;
    }

    private PsiElement getFirstNonWhitespace(PsiFile file, int offset) {
        if (offset <= 0) {
            return null;
        }
        PsiElement elementAt = file.findElementAt(offset);
        if (elementAt == null || elementAt.getNode().getElementType() == TokenType.WHITE_SPACE) {
            return getFirstNonWhitespace(file, offset - 1);
        }
        return elementAt;
    }
}
