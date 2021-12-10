package com.misset.opp.odt.formatter;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class ODTEnterHandlerDelegateAdapter extends EnterHandlerDelegateAdapter {

    @Override
    public Result preprocessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull Ref<Integer> caretOffset, @NotNull Ref<Integer> caretAdvance, @NotNull DataContext dataContext, EditorActionHandler originalHandler) {
        return super.preprocessEnter(file, editor, caretOffset, caretAdvance, dataContext, originalHandler);
    }

    @Override
    public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
        return super.postProcessEnter(file, editor, dataContext);
    }

    @Override
    public boolean invokeInsideIndent(int newLineCharOffset, @NotNull Editor editor, @NotNull DataContext dataContext) {
        return super.invokeInsideIndent(newLineCharOffset, editor, dataContext);
    }
}
