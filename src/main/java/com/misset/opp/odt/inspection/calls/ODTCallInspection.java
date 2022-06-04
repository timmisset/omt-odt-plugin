package com.misset.opp.odt.inspection.calls;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTSignature;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQualifiedUriStep;
import com.misset.opp.resolvable.Callable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public class ODTCallInspection extends LocalInspectionTool {

    protected static final String ROOT_INDICATOR_EXPECTED = "Path should start with root indicator (forward slash)";

    @Override
    public @Nullable
    @Nls String getStaticDescription() {
        return "Inspect calls made from the ODT language to any callable element:" +
                "- OMT Runnables\n" +
                "- BuiltIn operators & commands\n" +
                "- Local commands (COMMIT, CANCEL etc)\n";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {

        return new PsiElementVisitor() {

            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCall) {
                    final ODTCall call = (ODTCall) element;

                    final Callable callable = call.getCallable();
                    if (callable != null) {
                        // if callable is null the unresolvable annotator will catch it
                        callable.validate(call, holder);
                    }
                    if (call instanceof ODTCommandCall) {
                        validateCommandCall((ODTCommandCall) call);
                    }
                }
            }

            private void validateCommandCall(ODTCommandCall call) {
                Optional.ofNullable(call.getSignature())
                        .map(ODTSignature::getSignatureArgumentList)
                        .stream()
                        .flatMap(Collection::stream)
                        .forEach(this::validateSignatureArgument);
            }

            private void validateSignatureArgument(ODTSignatureArgument signatureArgument) {
                PsiElement deepestFirst = PsiTreeUtil.getDeepestFirst(signatureArgument);
                if (PsiTreeUtil.getParentOfType(deepestFirst,
                        ODTSignatureArgument.class,
                        ODTResolvableQualifiedUriStep.class) instanceof ODTResolvableQualifiedUriStep) {
                    holder.registerProblem(signatureArgument, ROOT_INDICATOR_EXPECTED, ProblemHighlightType.ERROR);
                }
            }
        };
    }
}
