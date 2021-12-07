package com.misset.opp.odt.inspection.calls;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.inspection.ModelAwarePsiElementVisitor;
import com.misset.opp.odt.psi.ODTDefineCommandStatement;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTDefinedCommandCallInspection extends LocalInspectionTool {
    @Override
    public @Nullable
    @Nls String getStaticDescription() {
        return "Inspect calls made from the ODT language to ODT Defined Commands";
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
                    if (callable instanceof ODTDefineCommandStatement) {
                        // if callable is null the unresolvable annotator will catch it
                        callable.validate(call, holder);
                    }
                }
            }
        };
    }
}
