package com.misset.opp.odt.inspection.calls.operators;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.builtin.operators.IIfOperator;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Code inspection for all unused declarations
 */
public class ODTOperatorInspectionIIf extends LocalInspectionTool {
    protected static final String UNNECESSARY_IIF = "Unnecessary IIF";
    protected static final String SIMPLIFY = "Simplify";
    protected static final String COMBINE = "Combine";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates specific ODT operator: IIf";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCall) {
                    inspectIIfOperator(holder, (ODTCall) element);
                }
            }
        };
    }

    private void inspectIIfOperator(@NotNull ProblemsHolder holder,
                                    @NotNull ODTCall call) {
        if (call.getCallable() == IIfOperator.INSTANCE && canBeSimplified(call)) {
            holder.registerProblem(call, UNNECESSARY_IIF, ProblemHighlightType.WARNING, getSimplifyQuickfix(call));
        }
    }

    private boolean canBeSimplified(@NotNull ODTCall call) {
        OntologyModel ontologyModel = OntologyModel.getInstance(call.getProject());
        List<ODTSignatureArgument> signatureArguments = call.getSignatureArguments();
        if (signatureArguments.size() == 2) {
            return OntologyModel.getInstance(call.getProject()).isBooleanInstance(signatureArguments.get(1).resolve());
        } else if (signatureArguments.size() == 3) {
            return (ontologyModel.isBooleanTrue(signatureArguments.get(1).resolveLiteral()) &&
                    ontologyModel.isBooleanInstance(signatureArguments.get(2).resolve())) ||
                    (ontologyModel.isBooleanFalse(signatureArguments.get(1).resolveLiteral()) &&
                            ontologyModel.isBooleanTrue(signatureArguments.get(2).resolveLiteral()));
        }
        return false;
    }

    @Nullable
    private LocalQuickFix getSimplifyQuickfix(@NotNull ODTCall call) {
        List<ODTSignatureArgument> signatureArguments = call.getSignatureArguments();
        ODTSignatureArgument firstArgument = call.getSignatureArgument(1);
        List<Literal> ifTrue = firstArgument.resolveLiteral();
        String condition = getCondition(call);
        OntologyModel ontologyModel = OntologyModel.getInstance(call.getProject());
        String query = null;
        if (signatureArguments.size() == 2) {
            if (ontologyModel.isBooleanFalse(ifTrue)) {
                // IIF(<condition>, false) => NOT <condition>
                query = "NOT " + condition;
            } else if (ontologyModel.isBooleanTrue(ifTrue)) {
                // IIF(<condition>, true) => NOT <condition>
                query = condition;
            } else if (ontologyModel.isBooleanInstance(firstArgument.resolve())) {
                // IIF(<condition>, <boolean>) => <condition> AND <boolean>
                query = condition + " AND " + firstArgument.getText();
            }
        } else if (signatureArguments.size() == 3) {
            ODTSignatureArgument secondArgument = call.getSignatureArgument(2);
            List<Literal> ifFalse = secondArgument.resolveLiteral();
            if (ontologyModel.isBooleanTrue(ifTrue) && ontologyModel.isBooleanFalse(ifFalse)) {
                // IIF(<condition>, true, false) => <condition>
                query = condition;
            } else if (ontologyModel.isBooleanTrue(ifFalse) && ontologyModel.isBooleanFalse(ifTrue)) {
                // IIF(<condition>, false, true) => NOT <condition>
                query = "NOT " + condition;
            } else if (ontologyModel.isBooleanTrue(ifTrue) && ontologyModel.isBooleanInstance(secondArgument.resolve())) {
                // IIF(<condition>, true, <boolean>) => <condition> OR <boolean>
                query = condition + " OR " + secondArgument.getText();
            }
        }
        return query != null ? getQuickFix(SIMPLIFY, query) : null;
    }

    private LocalQuickFix getQuickFix(String familyName, String query) {

        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return familyName;
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                final ODTQuery replacement = ODTElementGenerator.getInstance(project).fromFile(query, ODTQuery.class);
                if (replacement != null) {
                    descriptor.getPsiElement().replace(replacement);
                }
            }
        };
    }

    private String getCondition(@NotNull ODTCall call) {
        return call.getSignatureValue(0);
    }

}
