package com.misset.opp.odt.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class IntroduceLocalVariableIntention implements IntentionAction {
    protected static final String TEXT = "Introduce local variable";
    private static final TokenSet LOOKBACK = TokenSet.orSet(
            TokenSet.WHITE_SPACE, TokenSet.create(ODTTypes.SEMICOLON)
    );
    private static final Class<? extends PsiElement>[] NON_STRICT_PARENTS = new Class[]{
            ODTScript.class, ODTVariableAssignment.class, ODTDefineQueryStatement.class
    };

    @Override
    public @IntentionName @NotNull String getText() {
        return getFamilyName();
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return TEXT;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return Optional.ofNullable(getStatement(editor, file))
                .map(statement -> PsiTreeUtil.getNonStrictParentOfType(statement, NON_STRICT_PARENTS))
                .map(container -> container instanceof ODTScript)
                .orElse(false);
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        ODTStatement statement = getStatement(editor, file);
        if (statement instanceof ODTResolvable) {
            String variableName = getVariableName((ODTResolvable) statement);
            String assignment = "VAR " + variableName + " = " + statement.getText();
            ODTDeclareVariable declareVariable = ODTElementGenerator.getInstance(project).fromFile(assignment, ODTDeclareVariable.class);
            if (declareVariable != null) {
                statement.replace(declareVariable);
            }
        }
    }

    private static String getVariableName(ODTResolvable resolvable) {
        return resolvable.resolve().stream()
                .map(OppModel.INSTANCE::toClass)
                .distinct()
                .findFirst()
                .map(Resource::getLocalName)
                .filter(s -> s.length() > 2)
                .map(s -> "$" + s.substring(0, 1).toLowerCase() + s.substring(1))
                .orElse("$VARIABLE");
    }

    private ODTStatement getStatement(Editor editor, PsiFile file) {
        int offset = editor.getCaretModel().getCurrentCaret().getOffset();
        return Optional.ofNullable(getElementAtCaret(file, offset))
                .map(element -> PsiTreeUtil.getParentOfType(element, ODTStatement.class))
                .orElse(null);
    }

    private PsiElement getElementAtCaret(PsiFile file, int offset) {
        PsiElement elementAt = file.findElementAt(offset);
        while (offset > 0 && elementAt != null && LOOKBACK.contains(PsiUtilCore.getElementType(elementAt))) {
            offset -= 1;
            elementAt = file.findElementAt(offset);
        }
        return elementAt;
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }
}
