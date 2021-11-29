package com.misset.opp.omt;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.NotNull;

public class RenameOMTVariableHandler extends EditorActionHandler {

    @Override
    protected boolean isEnabledForCaret(@NotNull Editor editor,
                                        @NotNull Caret caret,
                                        DataContext dataContext) {
        return super.isEnabledForCaret(editor, caret, dataContext);
    }
}
