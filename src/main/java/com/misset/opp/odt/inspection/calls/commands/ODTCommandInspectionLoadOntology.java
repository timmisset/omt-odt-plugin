package com.misset.opp.odt.inspection.calls.commands;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.builtin.commands.LoadOntologyCommand;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableSignatureArgument;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.psi.PsiCall;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Code inspection for the specific Builtin Command LoadOntology which requires detailed information about the Call, rather than the
 * Callable which is why it is handled in the OMT language.
 * <p>
 * Basic validation on call signature (number of arguments etc.) is already handled by the Builtin Command
 */
public class ODTCommandInspectionLoadOntology extends LocalInspectionTool {
    protected static final String REQUIRES_A_REFERENCE_TO_AN_ONTOLOGY = "Requires a reference to an !Ontology";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates Builtin Command: @LOAD_ONTOLOGY for proper usage";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCall && ((ODTCall) element).getCallable() == LoadOntologyCommand.INSTANCE) {
                    final ODTCall call = (ODTCall) element;

                    final ODTResolvableSignatureArgument signatureArgument = call.getSignatureArgument(0);
                    final ODTCall signatureCall = PsiTreeUtil.findChildOfType(signatureArgument, ODTCall.class);
                    if (signatureCall != null) {
                        Boolean isValid = Optional.of(signatureCall)
                                .map(PsiCall::getCallable)
                                .map(Callable::getType)
                                .map(CallableType.ONTOLOGY::equals)
                                .orElse(false);
                        if (Boolean.FALSE.equals(isValid)) {
                            registerProblem(signatureCall);
                        }
                    }
                }
            }

            private void registerProblem(PsiElement element) {
                holder.registerProblem(element, REQUIRES_A_REFERENCE_TO_AN_ONTOLOGY, ProblemHighlightType.ERROR);
            }
        };
    }
}
