package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.PsiRelationshipUtil;
import com.misset.opp.resolvable.Variable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInspection.ProblemHighlightType.WARNING;

public class ODTUnusedVariableAssignmentInspection extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspect variable assignments usage.\n";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof ODTVariableAssignment)) {
                    return;
                }
                ODTVariableAssignment variable = (ODTVariableAssignment) element;
                variable.getVariableList()
                        .forEach(assignedVariable -> {
                            String name = assignedVariable.getName();
                            if (name == null || name.startsWith("$_")) {
                                return;
                            }
                            ODTScript scope = PsiTreeUtil.getTopmostParentOfType(element, ODTScript.class);
                            Variable declared = assignedVariable.getDeclared();
                            if (!(declared instanceof PsiElement)) {
                                return;
                            }
                            PsiElement declaredPsiElement = (PsiElement) declared;
                            if (!PsiTreeUtil.isAncestor(scope, declaredPsiElement, true)) {
                                return;
                            }
                            boolean isUsed = PsiTreeUtil.findChildrenOfType(scope, ODTVariable.class)
                                    .stream()
                                    .filter(usedVariable -> name.equals(usedVariable.getName()))
                                    .map(PsiRelationshipUtil::getRelatedElements)
                                    .anyMatch(variables -> variables.stream().anyMatch(assignedVariable::equals));
                            if (!isUsed) {
                                holder.registerProblem(
                                        assignedVariable,
                                        name + " is assigned but it's value is never read",
                                        WARNING);
                            }
                        });
            }


        };
    }
}
