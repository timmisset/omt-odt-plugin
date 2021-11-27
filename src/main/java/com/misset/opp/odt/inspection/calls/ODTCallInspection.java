package com.misset.opp.odt.inspection.calls;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.inspection.ModelAwarePsiElementVisitor;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Inspection of all Call elements by their Callable counterparts
 * While the Callable can validate many things it doesn't know the context in which the call is made.
 * <p>
 * This is especially true for the Builtin Commands & Operators that are part of the generic package
 *
 * @see com.misset.opp.callable
 * Any specific inspection to such a Builtin member should be done by a dedicated LocalInspection in the odt package instead
 */
public class ODTCallInspection extends LocalInspectionTool {
    @Override
    public @Nullable
    @Nls String getStaticDescription() {
        return "Inspect calls made from the ODT language to any callable:<br>" +
                "- ODT Command</br>" +
                "- ODT Query</br>" +
                "- ODT Builtin</br>" +
                "- OMT Runnable</br>" +
                "- ODT Standalone Query";
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
                    if (callable != null) {
                        // if callable is null the unresolvable annotator will catch it
                        callable.validate(call, holder);
                    }
                }
            }
        };
    }

    private LocalDateTime dateTime;

    @Override
    public void inspectionStarted(@NotNull LocalInspectionToolSession session,
                                  boolean isOnTheFly) {
        dateTime = LocalDateTime.now();
    }

    @Override
    public void inspectionFinished(@NotNull LocalInspectionToolSession session,
                                   @NotNull ProblemsHolder problemsHolder) {
        Logger.getInstance(ODTCallInspection.class)
                .warn("Inspection took: " + Duration.between(dateTime, LocalDateTime.now()).toMillis() + " ms");
    }
}
