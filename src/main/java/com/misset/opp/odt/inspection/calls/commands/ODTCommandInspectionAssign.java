package com.misset.opp.odt.inspection.calls.commands;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.builtin.commands.AssignCommand;
import com.misset.opp.odt.inspection.ModelAwarePsiElementVisitor;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableSignatureArgument;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
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
    protected static final String UNKNOWN_PREDICATE = "Unknown predicate";
    protected static final String ROOT_INDICATOR_EXPECTED = "Path should start with root indicator (forward slash)";

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
                    if (((ODTCall) element).getCallable() != AssignCommand.INSTANCE) {
                        return;
                    }
                    inspectAssign(holder, (ODTCall) element);
                }
            }
        };
    }

    private void inspectAssign(@NotNull ProblemsHolder holder,
                               @NotNull ODTCall call) {
        if (call.getNumberOfArguments() >= 3 && call.getNumberOfArguments() % 2 != 0) {
            final Set<OntResource> subject = call.resolveSignatureArgument(0);
            if (subject.isEmpty() || subject.contains(OppModel.INSTANCE.OWL_THING_INSTANCE)) {
                return;
            }

            for (int i = 1; i < call.getNumberOfArguments(); i = i + 2) {

                final ODTResolvableSignatureArgument predicateArgument = call.getSignatureArgument(i);
                if (predicateArgument == null) {
                    return;
                }
                if (PsiTreeUtil.getDeepestFirst(predicateArgument).getNode().getElementType() != ODTTypes.FORWARD_SLASH) {
                    holder.registerProblem(predicateArgument, ROOT_INDICATOR_EXPECTED, ProblemHighlightType.ERROR);
                    return;
                }

                if (PsiTreeUtil.findChildOfType(predicateArgument, ODTQueryReverseStep.class) != null) {
                    holder.registerProblem(predicateArgument,
                            CANNOT_ASSIGN_USING_A_REVERSE_PATH, ProblemHighlightType.ERROR);
                    return;
                }

                final List<Property> predicates = call.resolveSignatureArgument(i)
                        .stream()
                        .map(resource -> OppModel.INSTANCE.getProperty(resource))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (predicates.size() == 0) {
                    holder.registerProblem(predicateArgument,
                            UNKNOWN_PREDICATE,
                            ProblemHighlightType.ERROR);
                    return;
                } else if (predicates.size() > 1) {
                    holder.registerProblem(predicateArgument,
                            EXPECTED_A_SINGLE_PREDICATE,
                            ProblemHighlightType.ERROR);
                    return;
                }
                final Property predicate = predicates.get(0);

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
