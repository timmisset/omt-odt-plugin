package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.AbstractBuiltin;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractBuiltInOperator extends AbstractBuiltin {

    protected static final List<String> IGNORE_CASE_FLAG = List.of("!ignoreCase");

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public String getCallId() {
        return getName();
    }

    @Override
    public boolean requiresInput() {
        return true;
    }

    @Override
    protected OntResource resolveSingle() {
        return null;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return resolve();
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resolve();
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        return resolveFrom(resources);
    }

    @Override
    protected Set<OntResource> resolveError(Set<OntResource> resources, PsiCall call) {
        return resources;
    }

    protected void validateIgnoreCaseFlagUsage(int dynamicArgumentIndex,
                                               PsiCall call,
                                               ProblemsHolder holder) {
        validateFlagUsage(dynamicArgumentIndex, call, holder, "true", "ignoreCase");
    }

    protected void validateFlagUsage(int dynamicArgumentIndex,
                                     PsiCall call,
                                     ProblemsHolder holder,
                                     String defaultValueForDynamicArgument,
                                     String flagName) {
        if (call.getFlag() != null && call.getNumberOfArguments() == dynamicArgumentIndex + 1) {
            holder.registerProblem(call.getCallSignatureArgumentElement(dynamicArgumentIndex),
                    "Using both a flag and parameter",
                    ProblemHighlightType.ERROR);
        }
        if (call.getNumberOfArguments() == dynamicArgumentIndex + 1) {
            String signatureValue = call.getSignatureValue(dynamicArgumentIndex);
            if (defaultValueForDynamicArgument.equals(signatureValue)) {
                holder.registerProblem(call.getCallSignatureArgumentElement(dynamicArgumentIndex),
                        "Use !" + flagName + " flag instead",
                        ProblemHighlightType.WEAK_WARNING);
            }
        }
    }

    protected void validateIgnoreCaseFlagIsUsedOnStrings(Set<OntResource> resources,
                                                         PsiCall call,
                                                         ProblemsHolder holder) {
        String flag = call.getFlag();
        if (!resources.isEmpty() &&
                flag != null &&
                flag.contains("!ignoreCase") &&
                !resources.contains(OntologyModelConstants.getXsdStringInstance())) {
            holder.registerProblem(call.getFlagElement(), "Using ignoreCase on non-string values", ProblemHighlightType.WARNING);
        }
    }

    /**
     * Validates that left-hand and right-hand arguments are compatible by extracting the left-hand and
     * right-hand based on the number of arguments. 1 == input + arg(0), 2 == arg(0) + arg(1).
     * Returns set from left-hand for additional type validations
     */
    protected Set<OntResource> validateLeftRightCompatible(PsiCall call, ProblemsHolder holder) {
        Pair<Set<OntResource>, Set<OntResource>> leftRight = getLeftRightFromArguments(call);
        if (leftRight.getFirst() == null || leftRight.getSecond() == null) {
            return Collections.emptySet();
        }
        OntologyValidationUtil.getInstance(holder.getProject()).validateCompatibleTypes(leftRight.getFirst(), leftRight.getSecond(), holder, call);
        return leftRight.getFirst();
    }

    protected Pair<Set<OntResource>, Set<OntResource>> getLeftRightFromArguments(PsiCall call) {
        final Set<OntResource> left;
        final Set<OntResource> right;
        if (call.getNumberOfArguments() == 1) {
            left = call.resolvePreviousStep();
            right = call.resolveSignatureArgument(0);
        } else if (call.getNumberOfArguments() == 2) {
            left = call.resolveSignatureArgument(0);
            right = call.resolveSignatureArgument(1);
        } else {
            return Pair.empty();
        }
        return Pair.create(left, right);
    }

    protected void validateCompatibleOutcomePossibilities(Pair<Set<OntResource>, Set<OntResource>> possibilities,
                                                          PsiCall call,
                                                          ProblemsHolder holder) {
        if (possibilities.getFirst().isEmpty() || possibilities.getSecond().isEmpty()) {
            return;
        }

        boolean b = OntologyModel.getInstance(call.getProject()).areCompatible(possibilities.getFirst(), possibilities.getSecond());
        if (!b) {
            holder.registerProblem(call,
                    "Possible outcomes are incompatible, not illegal but it smells fishy",
                    ProblemHighlightType.WEAK_WARNING);
        }
    }

    @Override
    public CallableType getType() {
        return CallableType.BUILTIN_OPERATOR;
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return true;
    }

    @Override
    protected List<String> getParameters() {
        return Collections.emptyList();
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        // do nothing if not overridden in implementation class
    }
}
