package com.misset.opp.odt.refactoring;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.introduce.inplace.AbstractInplaceIntroducer;
import com.intellij.refactoring.introduce.inplace.OccurrencesChooser;
import com.misset.opp.exception.OMTODTPluginException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTStatement;
import com.misset.opp.odt.psi.impl.variable.ODTBaseDeclaredVariable;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.LinkedHashSet;

/**
 * Class to start the refactoring process after a variable is introduced
 */
public class ODTInplaceVariableIntroducer extends AbstractInplaceIntroducer<ODTBaseDeclaredVariable, PsiElement> {
    @SuppressWarnings("java:S107")
    public ODTInplaceVariableIntroducer(Project project,
                                        Editor editor,
                                        @Nullable ODTResolvable statement,
                                        @Nullable ODTBaseDeclaredVariable localVariable,
                                        PsiElement[] occurrences,
                                        @NlsContexts.Command String title,
                                        FileType languageFileType, OccurrencesChooser.ReplaceChoice choice) {
        super(project, editor, statement, localVariable, occurrences, title, languageFileType);
        this.statement = statement;
        this.suggestedNames = new LinkedHashSet<>();
        this.choice = choice;

        if (statement == null) {
            throw new OMTODTPluginException("Statement must not be null");
        }
        new ODTNameSuggestionProvider().getSuggestedNames(statement, statement, suggestedNames);
    }

    private final ODTResolvable statement;
    private final LinkedHashSet<String> suggestedNames;
    private ODTBaseDeclaredVariable declaredVariable = null;
    private final OccurrencesChooser.ReplaceChoice choice;

    @Override
    protected @Nullable @NonNls String getActionName() {
        return "Introduce Variable";
    }

    @Nullable
    @Override
    protected ODTBaseDeclaredVariable createFieldToStartTemplateOn(boolean replaceAll, String @NotNull [] names) {
        ODTBaseDeclaredVariable createdVariable = WriteCommandAction.runWriteCommandAction(myProject, (Computable<ODTBaseDeclaredVariable>) () -> {
            String variableName = getInitialName();
            String assignment = "VAR " + variableName + " = " + statement.getText();
            ODTBaseDeclaredVariable variable = ODTElementGenerator.getInstance(myProject).fromFile(assignment, ODTBaseDeclaredVariable.class);
            declaredVariable = (ODTBaseDeclaredVariable) statement.replace(variable);
            return declaredVariable;
        });
        myLocalMarker = createMarker(createdVariable.getNameIdentifier());
        myLocalVariable = createdVariable;
        return createdVariable;
    }

    @Override
    protected String @NotNull [] suggestNames(boolean replaceAll, @Nullable ODTBaseDeclaredVariable variable) {
        return suggestedNames.toArray(String[]::new);
    }

    @Override
    protected void performIntroduce() {
        // do nothing
    }

    @Override
    public boolean isReplaceAllOccurrences() {
        return choice.isAll();
    }

    @Override
    public void setReplaceAllOccurrences(boolean allOccurrences) {
        // do nothing
    }

    @Override
    protected @Nullable JComponent getComponent() {
        // implement to add an additional JComponent in a prompt, such as the 'final' option when
        // introducing a variable in Java / Groovy
        return null;
    }

    @Override
    protected void saveSettings(@NotNull ODTBaseDeclaredVariable variable) {
        this.declaredVariable = variable;
    }

    @Nullable
    @Override
    protected ODTBaseDeclaredVariable getVariable() {
        if (myLocalMarker == null) return null;

        int offset = myLocalMarker.getStartOffset();
        PsiElement at = myExpr.getContainingFile().findElementAt(offset);
        return PsiTreeUtil.getParentOfType(at, ODTBaseDeclaredVariable.class);
    }

    @Override
    public ODTResolvable restoreExpression(@NotNull PsiFile containingFile,
                                           @NotNull ODTBaseDeclaredVariable variable,
                                           @NotNull RangeMarker marker,
                                           @Nullable String exprText) {
        return (ODTResolvable) ODTElementGenerator.getInstance(variable.getProject())
                .fromFile(exprText, ODTStatement.class);
    }

    @Override
    public String getInitialName() {
        if (suggestedNames.isEmpty()) {
            return "$variable";
        }
        return suggestedNames.iterator().next();
    }
}

