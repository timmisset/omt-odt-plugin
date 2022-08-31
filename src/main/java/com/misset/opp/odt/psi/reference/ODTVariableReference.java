package com.misset.opp.odt.psi.reference;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.LocalQuickFixProvider;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.InspectionMessage;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ODTVariableReference extends ODTPolyReferenceBase<ODTVariable> implements LocalQuickFixProvider, EmptyResolveMessageProvider {
    private static final Key<ODTVariableReference> REFERENCE = new Key<>("REFERENCE");

    public static ODTVariableReference forVariable(ODTVariable variable) {
        if (!REFERENCE.isIn(variable)) {
            ODTVariableReference odtVariableReference = new ODTVariableReference(variable);
            REFERENCE.set(variable, odtVariableReference);
            return odtVariableReference;
        }
        return REFERENCE.get(variable);
    }

    private ODTVariableReference(@NotNull ODTVariable element) {
        super(element, TextRange.allOf(element.getText()), false);
    }

    @Override
    public @Nullable PsiElement resolve() {
        return resolve(true);
    }

    @Override
    public PsiElement resolve(boolean resolveToOriginalElement) {
        return resolve(resolveToOriginalElement, true);
    }

    @Override
    public PsiElement resolve(boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        return getResultByProximity(resolveToOriginalElement, resolveToFinalElement);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return multiResolve(true, true);
    }

    public ResolveResult @NotNull [] multiResolve(boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        if (!(myElement.getContainingFile() instanceof ODTFile)) {
            return ResolveResult.EMPTY_ARRAY;
        } else {
            ODTFile file = (ODTFile) myElement.getContainingFile();
            List<PsiVariable> variables = file.getVariables(myElement.getName())
                    .stream()
                    .filter(PsiVariable.class::isInstance)
                    .map(PsiVariable.class::cast)
                    .filter(psiVariable -> file.isAccessible(myElement, psiVariable))
                    .collect(Collectors.toList());
            return toResults(variables, resolveToOriginalElement, resolveToFinalElement);
        }
    }

    // required to preserve valid reference text range when backspacing an element
    @Override
    public @NotNull TextRange getRangeInElement() {
        return TextRange.allOf(myElement.getText());
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return getElement().setName(newElementName);
    }

    @Override
    public @InspectionMessage @NotNull String getUnresolvedMessagePattern() {
        return "Cannot resolve variable '" + getElement().getName() + "'";
    }

    @Override
    public LocalQuickFix @Nullable [] getQuickFixes() {
        return new LocalQuickFix[]{
                new LocalQuickFix() {
                    @Override
                    public @IntentionFamilyName @NotNull String getFamilyName() {
                        return "Declare variable";
                    }

                    @Override
                    public @IntentionName @NotNull String getName() {
                        return "Declare variable";
                    }

                    @Override
                    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                        PsiElement psiElement = descriptor.getPsiElement();
                        ODTScriptLine scriptLine = PsiTreeUtil.getTopmostParentOfType(psiElement, ODTScriptLine.class);
                        if (scriptLine == null) {
                            return;
                        }
                        ODTScript script = (ODTScript) scriptLine.getParent();
                        if (script == null) {
                            return;
                        }

                        ODTScriptLine variableDeclaration = ODTElementGenerator.getInstance(project).createVariableDeclaration(getElement().getName());
                        script.addBefore(variableDeclaration, scriptLine);
                    }
                }
        };
    }

}
