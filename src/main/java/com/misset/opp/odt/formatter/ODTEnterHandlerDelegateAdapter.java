package com.misset.opp.odt.formatter;

import com.google.common.base.Strings;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.impl.CaretImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTIgnored;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLScalarListImpl;

import java.util.List;
import java.util.Optional;

public class ODTEnterHandlerDelegateAdapter extends EnterHandlerDelegateAdapter {

    private int moveCaretAfterInsert = 0;
    private int minimalLineOffset = 0;
    @Override
    public Result preprocessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull Ref<Integer> caretOffset, @NotNull Ref<Integer> caretAdvance, @NotNull DataContext dataContext, EditorActionHandler originalHandler) {
        if (!(file instanceof ODTFile)) {
            return Result.Continue;
        }
        moveCaretAfterInsert = 0;

        PsiElement elementAt = file.findElementAt(caretOffset.get());
        IElementType prevElementType = Optional.ofNullable(elementAt)
                .map(PsiElement::getPrevSibling)
                .map(PsiElement::getNode)
                .map(ASTNode::getElementType)
                .orElse(null);

        if (prevElementType == ODTIgnored.DOC_COMMENT_START) {
            // insert javadoc block, an already complete block would not be parsed to this element type
            insert(file.getProject(), editor, "* \n */", caretOffset.get());
            moveCaretAfterInsert = 3;
        }
        minimalLineOffset = getMinimalLineOffset(file);
        return Result.Continue;
    }

    @Override
    public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
        if (moveCaretAfterInsert > 0) {
            editor.getCaretModel().getCurrentCaret().moveCaretRelatively(moveCaretAfterInsert, 0, false, true);
        } else {
            if (minimalLineOffset > 0) {
                Object caret = dataContext.getData("caret");
                if (caret instanceof CaretImpl) {
                    CaretImpl currentCaret = (CaretImpl) caret;
                    if (currentCaret.getVisualPosition().column == 0) {
                        // known issue with continuation indent when injected
                        Document hostDocument = getHostDocument(file);
                        int indentSize = minimalLineOffset + 4; // + 4 is fixed size for continuation indent
                        insert(file.getProject(),
                                hostDocument,
                                Strings.repeat(" ", indentSize),
                                currentCaret.getOffset());
                    }
                }
            }
        }
        return Result.Continue;
    }

    private int getMinimalLineOffset(@NotNull PsiFile file) {
        PsiLanguageInjectionHost host = ((ODTFileImpl) file).getHost();
        if (host == null) {
            return 0;
        }
        int textOffset = getInjectionStart(host);
        Document hostDocument = getHostDocument(file);
        if (hostDocument == null) {
            return 0;
        }
        int lineNumber = hostDocument.getLineNumber(textOffset);
        return textOffset - hostDocument.getLineStartOffset(lineNumber);
    }

    private int getInjectionStart(PsiLanguageInjectionHost host) {
        if (host instanceof YAMLScalarListImpl) {
            List<TextRange> contentRanges = ((YAMLScalarListImpl) host).getTextEvaluator().getContentRanges();
            if (contentRanges.size() > 0) {
                return host.getTextOffset() + contentRanges.get(0).getStartOffset();
            }
        }
        return host.getTextOffset();
    }

    private Document getHostDocument(@NotNull PsiFile file) {
        PsiLanguageInjectionHost host = ((ODTFileImpl) file).getHost();
        return PsiDocumentManager.getInstance(file.getProject()).getDocument(host.getContainingFile());
    }

    @Override
    public boolean invokeInsideIndent(int newLineCharOffset, @NotNull Editor editor, @NotNull DataContext dataContext) {
        return super.invokeInsideIndent(newLineCharOffset, editor, dataContext);
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
