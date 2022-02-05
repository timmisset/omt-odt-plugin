package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableCall;
import com.misset.opp.odt.psi.reference.ODTCallReference;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Code annotator for all unused declarations
 */
public class ODTCodeInspectionUnresolvableReference extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspects references set on variables and calls. If not resolvable it will show UNKNOWN errors:<br>" +
                "Cannot resolve [type] '[name]'";
    }

    private static final Logger LOGGER = Logger.getInstance(ODTCodeInspectionUnresolvableReference.class);

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTVariable) {
                    LoggerUtil.runWithLogger(LOGGER, "annotate variable " + ((ODTVariable) element).getName(), () -> annotateVariable(holder, (ODTVariable) element));
                } else if (element instanceof ODTResolvableCall) {
                    LoggerUtil.runWithLogger(LOGGER, "annotate call " + ((ODTResolvableCall) element).getName(), () -> annotateCall(holder, (ODTResolvableCall) element));
                }
            }
        };
    }

    private void annotateVariable(@NotNull ProblemsHolder holder,
                                  @NotNull ODTVariable variable) {
        PsiReference reference = variable.getReference();
        if (reference != null && !variable.isDeclaredVariable() && variable.getDeclared() == null) {
            holder.registerProblem(reference);
        }
    }

    private void annotateCall(@NotNull ProblemsHolder holder,
                              @NotNull ODTResolvableCall call) {
        final Callable callable = call.getCallable();
        ODTCallReference reference = call.getReference();
        if (reference != null && callable == null) {
            holder.registerProblem(reference);
        }
    }
}
