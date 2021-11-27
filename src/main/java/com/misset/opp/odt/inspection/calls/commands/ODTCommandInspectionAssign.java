package com.misset.opp.odt.inspection.calls.commands;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.callable.builtin.commands.ForEachCommand;
import com.misset.opp.odt.inspection.ModelAwarePsiElementVisitor;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableSignatureArgument;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.validation.TTLValidationUtil;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Code inspection for the specific Builtin Command Assign which requires detailed information about the Call, rather than the
 * Callable which is why it is handled in the OMT language.
 * <p>
 * Basic validation on call signature (number of arguments etc.) is already handled by the Builtin Command
 */
public class ODTCommandInspectionAssign extends LocalInspectionTool {
    protected static final String CANNOT_ASSIGN_USING_A_REVERSE_PATH = "Cannot assign using a REVERSE path";
    protected static final String EXPECTED_A_SINGLE_PREDICATE = "Expected a single predicate";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates Builtin Command: @ASSIGN for proper usage";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new ModelAwarePsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCall) {
                    if (((ODTCall) element).getCallable() != ForEachCommand.INSTANCE) {
                        return;
                    }
                    inspectForEach(holder, (ODTCall) element);
                }
            }
        };
    }

    private void inspectForEach(@NotNull ProblemsHolder holder,
                                @NotNull ODTCall call) {
        if (call.numberOfArguments() >= 3 && call.numberOfArguments() % 2 != 0) {
            final Set<OntResource> subject = call.resolveSignatureArgument(0);
            for (int i = 1; i < call.numberOfArguments(); i = i + 2) {

                final ODTResolvableSignatureArgument predicateArgument = call.getSignatureArgument(i);
                if (predicateArgument == null) {
                    return;
                }

                if (PsiTreeUtil.findChildOfType(predicateArgument, ODTQueryReverseStep.class) != null) {
                    holder.registerProblem(predicateArgument,
                            CANNOT_ASSIGN_USING_A_REVERSE_PATH, ProblemHighlightType.ERROR);
                    return;
                }

                final List<OntResource> predicates = call.resolveSignatureArgument(i)
                        .stream()
                        .filter(OntResource::isProperty)
                        .collect(Collectors.toList());

                if (predicates.size() != 1 || predicates.stream().noneMatch(OntResource::isProperty)) {
                    holder.registerProblem(predicateArgument,
                            EXPECTED_A_SINGLE_PREDICATE,
                            ProblemHighlightType.ERROR);
                    return;
                }
                final OntProperty predicate = predicates.get(0).asProperty();

                final Set<OntResource> object = OppModel.INSTANCE.listObjects(subject, predicate);
                if (object.isEmpty()) {
                    holder.registerProblem(predicateArgument,
                            "Could not traverse FORWARD using predicate: " + predicate.toString());
                }

                final Set<OntResource> value = call.resolveSignatureArgument(i + 1);
                TTLValidationUtil.validateCompatibleTypes(object, value, holder, call.getSignatureArgument(i));
            }
        }
    }
}
