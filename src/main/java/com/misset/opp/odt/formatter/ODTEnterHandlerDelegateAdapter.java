package com.misset.opp.odt.formatter;

import com.google.common.base.Strings;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.lang.ASTNode;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.impl.CaretImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTIgnored;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ODTEnterHandlerDelegateAdapter extends EnterHandlerDelegateAdapter {

    private int moveCaretAfterInsert = 0;
    private int minimalLineOffset = 0;

    @Override
    @SuppressWarnings("java:S2637")
    public Result preprocessEnter(@NotNull PsiFile file,
                                  @NotNull Editor editor,
                                  @NotNull Ref<Integer> caretOffset,
                                  @NotNull Ref<Integer> caretAdvance,
                                  @NotNull DataContext dataContext,
                                  EditorActionHandler originalHandler) {
        if (!(file instanceof ODTFile)) {
            return Result.Continue;
        }
        ODTFile odtFile = (ODTFile) file;
        moveCaretAfterInsert = 0;

        PsiElement elementAt = odtFile.findElementAt(caretOffset.get());
        IElementType prevElementType = Optional.ofNullable(elementAt)
                .map(PsiElement::getPrevSibling)
                .map(PsiElement::getNode)
                .map(ASTNode::getElementType)
                // suppressing warning S2637
                .orElse(null);

        if (prevElementType == ODTIgnored.DOC_COMMENT_START) {
            // insert javadoc block, an already complete block would not be parsed to this element type
            insert(odtFile.getProject(), editor, "* \n */", caretOffset.get());
            moveCaretAfterInsert = 3;
        }
        minimalLineOffset = odtFile.getLineOffsetInParent();
        return Result.Continue;
    }

    @Override
    public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
        if (!(file instanceof ODTFile)) {
            return Result.Continue;
        }
        if (moveCaretAfterInsert > 0) {
            editor.getCaretModel().getCurrentCaret().moveCaretRelatively(moveCaretAfterInsert, 0, false, true);
        } else {
            moveIndent(dataContext, file);
        }
        return Result.Continue;
    }

    private void moveIndent(@NotNull DataContext dataContext, @NotNull PsiFile file) {
        if (minimalLineOffset > 0) {
            Object caret = dataContext.getData("caret");
            if (caret instanceof CaretImpl) {
                CaretImpl currentCaret = (CaretImpl) caret;
                if (currentCaret.getVisualPosition().column == 0) {
                    PsiFile topLevelFile = InjectedLanguageManager.getInstance(file.getProject()).getTopLevelFile(file);
                    Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(topLevelFile);
                    // known issue with continuation indent when injected
                    int indentSize = minimalLineOffset + 4; // + 4 is fixed size for continuation indent
                    insert(file.getProject(), document, Strings.repeat(" ", indentSize), currentCaret.getOffset());
                }
            }
        }
    }

    private void insert(Project project,
                        @NotNull Editor editor,
                        @NotNull String text,
                        int caretOffset) {
        final Document document = editor.getDocument();
        insert(project, document, text, caretOffset);
    }

    private void insert(Project project, Document document, @NotNull String text, int caretOffset) {
        PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
        documentManager.doPostponedOperationsAndUnblockDocument(document);
        document.insertString(caretOffset, text);
        documentManager.commitDocument(document);
    }
}
