package com.misset.opp.odt.inspection.calls;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.inspection.ModelAwarePsiElementVisitor;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.omt.psi.OMTCallable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTOMTCallInspection extends LocalInspectionTool {
    @Override
    public @Nullable
    @Nls String getStaticDescription() {
        return "Inspect calls made from the ODT language to callable elements in the OMT language (Activities, Procedures, StandaloneQueries)";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {

        return new ModelAwarePsiElementVisitor() {

            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCall) {
                    final ODTCall call = (ODTCall) element;
                    final Callable callable = call.getCallable();
                    if (callable instanceof OMTCallable) {
                        // if callable is null the unresolvable annotator will catch it
                        callable.validate(call, holder);
                    }
                }
            }
        };
    }
}
